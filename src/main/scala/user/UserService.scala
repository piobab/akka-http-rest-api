package user

import authentication.UserAuthRepository
import core.authentication.Identity
import redis.RedisClient
import token.TokenRepository
import core.authentication.UserSerializer._

import scala.concurrent.{Future, ExecutionContext}

class UserService(tokenRepo: TokenRepository, userAuthRepo: UserAuthRepository, userRepo: UserRepository)
                 (implicit ec: ExecutionContext, redisClient: RedisClient) {

  def updateLoggedUser(identity: Identity, request: UserUpdateRequest): Future[Unit] = {
    val userId = identity.user.id
    userRepo.update(userId, request.firstName, request.lastName, request.gender, request.streetAddress, request.postalCode, request.postalCity)
  }

  def getUser(id: Long): Future[Either[String, User]] = {
    userRepo.findByUserId(id).map {
      case Some(user) => Right(user)
      case None => Left(s"User with id=$id does not exist")
    }
  }

  def getUsers(offset: Long, limit: Int): Future[UsersResponse] = {
    userRepo.getUsers(offset, limit).map(users => UsersResponse(users.size, users))
  }

  def deleteUser(id: Long): Future[Either[String, Unit]] = {
    userRepo.findByUserId(id).flatMap {
      case Some(user) =>
        for {
          _ <- userAuthRepo.deleteByUserId(id)
          _ <- userRepo.delete(id)
          _ <- deleteTokens(id)
        } yield Right(())
      case None => Future.successful(Left(s"User with id=$id does not exist"))
    }
  }

  /**
   * Delete all valid tokens for user id from Redis and then update those tokens in database as invalid.
   * @param userId user id
   * @return
   */
  private def deleteTokens(userId: Long): Future[Unit] = {
    for {
      tokens <- tokenRepo.getValidTokens(userId)
      _ <- redisClient.del(tokens.map(_.value): _*)
      _ <- tokenRepo.update(userId, false)
    } yield ()
  }

  def updateRole(userId: Long, identity: Identity, request: UserRoleUpdateRequest): Future[Either[String, Unit]] = {
    userRepo.findByUserId(userId).flatMap {
      case Some(user) =>
        for {
          _ <- userRepo.update(userId, request.role)
          _ <- updateTokens(userId, request.role)
        } yield Right(())
      case None => Future.successful(Left(s"User with id=$userId does not exist"))
    }
  }

  /**
   * Update all valid tokens for user id in Redis with new role.
   * @param userId user id
   * @param role new role
   * @return
   */
  private def updateTokens(userId: Long, role: Int): Future[Unit] = {
    for {
      tokens <- tokenRepo.getValidTokens(userId)
      tokensUsers <- Future.successful(tokens.map(token => token.value -> Identity.User(userId, role)).toMap)
      _ <- redisClient.mset(tokensUsers)
    } yield ()
  }
}
