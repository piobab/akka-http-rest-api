package authentication

import org.joda.time.DateTime
import org.scalatest.Matchers
import user.{User, UserRepository}
import utils.FlatSpecWithSql

import scala.concurrent.ExecutionContext.Implicits.global

class UserAuthRepositorySpec extends FlatSpecWithSql with Matchers {
  behavior of "UserAuthRepository"

  val userRepo = new UserRepository
  val userAuthRepo = new UserAuthRepository

  it should "add new user auth" in {
    val user = User(None, DateTime.now().getMillis, "Jan", "Kowalski", 1, None, None, None, 0)
    val userId = userRepo.insert(user).futureValue.id.get

    val userAuth = UserAuth(None, "jan.kowalski@gmail.com", "password", userId)
    val userAuthId = userAuthRepo.insert(userAuth).futureValue.id.get

    userAuthRepo.findByUserAuthId(userAuthId).futureValue should be('defined)
  }

  it should "find user auth by email" in {
    val user = User(None, DateTime.now().getMillis, "Marek", "Nowak", 1, None, None, None, 0)
    val userId = userRepo.insert(user).futureValue.id.get

    val email = "mnowak@gmail.com"
    val userAuth = UserAuth(None, email, "password", userId)
    val userAuthId = userAuthRepo.insert(userAuth).futureValue.id.get

    userAuthRepo.findByUserEmail(email).futureValue should equal(Some(userAuth.copy(id = Some(userAuthId))))
  }

  it should "delete user auth" in {
    val user = User(None, DateTime.now().getMillis, "Marek", "Nowak", 1, None, None, None, 0)
    val userId = userRepo.insert(user).futureValue.id.get

    val userAuth = UserAuth(None, "mnowak@gmail.com", "password", userId)
    userAuthRepo.insert(userAuth).futureValue.id.get

    userAuthRepo.deleteByUserId(userId).futureValue

    userAuthRepo.findByUserId(userId).futureValue should be(None)
  }
}
