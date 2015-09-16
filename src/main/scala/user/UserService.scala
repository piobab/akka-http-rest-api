package user

import user.be.UserEntity
import user.repository.UserRepository

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by piobab on 16.09.15.
 */
class UserService(userRepo: UserRepository)(implicit ec: ExecutionContext) {

  def createDefaultUser(userAuthId: Long): Future[Long] = {
    val userEntity = UserEntity(0, None, None, None, None, None, None, userAuthId)
    userRepo.insert(userEntity).map(_.id)
  }
}
