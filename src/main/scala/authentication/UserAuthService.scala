package authentication

import authentication.be.UserAuthEntity
import authentication.repository.UserAuthRepository
import org.joda.time.DateTime

import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by piobab on 14.09.15.
 */
class UserAuthService(userAuthRepo: UserAuthRepository)(implicit ec: ExecutionContext) {

  def register(email: String, password: String): Future[Long] = {
    val userAuthEntity = UserAuthEntity(0, DateTime.now().getMillis, email, password)
    userAuthRepo.insert(userAuthEntity).map(_.id)
  }
}
