package authentication

import authentication.be.UserAuthEntity
import authentication.repository.UserAuthRepository
import org.joda.time.DateTime
import redis.RedisClient
import user.be.UserEntity
import user.repository.UserRepository

import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by piobab on 14.09.15.
 */
class UserAuthService(userAuthRepo: UserAuthRepository, userRepo: UserRepository)(implicit ec: ExecutionContext, redisClient: RedisClient) {

  def register(email: String, password: String): Future[Long] = {
    for {
      userEntity <- userRepo.insert(UserEntity(0, None, None, None, None, None, None))
      userAuthEntity <- userAuthRepo.insert(UserAuthEntity(0, DateTime.now().getMillis, email, password, userEntity.id))
      result <- redisClient.setnx("authToken", userAuthEntity.id)
    } yield userAuthEntity.id
  }
}
