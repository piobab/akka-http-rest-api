package user

import authentication.UserAuthRepository
import core.authentication.Identity
import redis.RedisClient
import core.authentication.UserSerializer._

import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by piobab on 16.09.15.
 */
class UserService(userAuthRepo: UserAuthRepository, userRepo: UserRepository)(implicit ec: ExecutionContext, redisClient: RedisClient) {

  def updateLoggedUser(identity: Identity, request: UserUpdateRequest): Future[Unit] = {
    val token = identity.authToken
    val user = identity.user.copy(
      firstName = request.firstName,
      lastName = request.lastName,
      gender = request.gender,
      streetAddress = request.streetAddress,
      postalCode = request.postalCode,
      postalCity = request.postalCity
    )

    for {
      _ <- userRepo.update(user)
      _ <- redisClient.setnx(token, user)
    } yield ()
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

  // TODO Should delete token for user
  def deleteUser(id: Long): Future[Either[String, Unit]] = {
    userRepo.findByUserId(id).flatMap {
      case Some(user) =>
        for {
          _ <- userAuthRepo.deleteByUserId(id)
          _ <- userRepo.delete(id)
        } yield Right(())
      case None => Future.successful(Left(s"User with id=$id does not exist"))
    }
  }

  // TODO Should update token for user
  def updateAdmin(userId: Long, request: UserAdminUpdateRequest): Future[Either[String, Unit]] = {
    userRepo.findByUserId(userId).flatMap {
      case Some(user) => userRepo.update(userId, request.admin).map(Right(_))
      case None => Future.successful(Left(s"User with id=$userId does not exist"))
    }
  }
}
