package authentication

import java.math.BigInteger
import java.security.SecureRandom

import authentication.UserAuthResult.{UserNotExists, UserExists, Success, InvalidData}
import core.authentication.Identity
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import redis.RedisClient
import token.TokenRepository
import user.{UserRepository, User}
import core.authentication.UserSerializer._

import scala.concurrent.{Future, ExecutionContext}

class UserAuthService(tokenRepo: TokenRepository, userAuthRepo: UserAuthRepository, userRepo: UserRepository)
                     (implicit ec: ExecutionContext, redisClient: RedisClient) {

  def register(request: UserRegistrationRequest): Future[UserAuthResult] = {
    if (!RegistrationValidator.isDataValid(request.email, request.password)) {
      Future.successful(InvalidData("E-mail or password is invalid"))
    } else {
      userAuthRepo.findByUserEmail(request.email).flatMap {
        case Some(userAuth) => Future.successful(UserExists("E-mail already in use"))
        case None =>
          val now = DateTime.now().getMillis
          val token = generateToken
          for {
            user <- userRepo.insert(User(None, now, request.firstName, request.lastName, request.gender, None, None, None, request.role))
            _ <- userAuthRepo.insert(UserAuth(None, request.email, hashPassword(request.password), user.id.get))
            _ <- tokenRepo.insert(token, user.id.get)
            _ <- redisClient.setnx(token, Identity.User(user.id.get, user.role))
          } yield {
            Success(token)
          }
      }
    }
  }

  def login(request: UserLoginRequest): Future[UserAuthResult] = {
    userAuthRepo.findByUserEmail(request.email).flatMap {
      case Some(userAuth) if checkPassword(request.password, userAuth.password) =>
        val token = generateToken
        for {
          Some(user) <- userRepo.findByUserId(userAuth.userId)
          _ <- tokenRepo.insert(token, user.id.get)
          _ <- redisClient.setnx(token, Identity.User(user.id.get, user.role))
        } yield {
          Success(token)
        }
      case Some(_) => Future.successful(InvalidData("Password is invalid"))
      case None => Future.successful(UserNotExists("E-mail does not exist"))
    }
  }

  private lazy val random = new SecureRandom()

  private def generateToken: String = new BigInteger(255, random).toString(32)

  private def hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(12))

  private def checkPassword(password: String, passwordHash: String): Boolean = BCrypt.checkpw(password, passwordHash)
}

trait UserAuthResult

object UserAuthResult {

  case class InvalidData(msg: String) extends UserAuthResult

  case class UserExists(msg: String) extends UserAuthResult

  case class UserNotExists(msg: String) extends UserAuthResult

  case class Success(token: String) extends UserAuthResult

}

