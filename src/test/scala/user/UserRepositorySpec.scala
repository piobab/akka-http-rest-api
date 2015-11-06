package user

import org.joda.time.DateTime
import org.scalatest.Matchers
import utils.FlatSpecWithSql

import scala.concurrent.ExecutionContext.Implicits.global

class UserRepositorySpec extends FlatSpecWithSql with Matchers {
  behavior of "UserRepository"

  val userRepo = new UserRepository

  it should "add new user" in {
    val user = User(None, DateTime.now().getMillis, "Jan", "Kowalski", 1, None, None, None, 0)
    val userId = userRepo.insert(user).futureValue.id.get
    userRepo.findByUserId(userId).futureValue should equal(Some(user.copy(id = Some(userId))))
  }

  it should "get users" in {
    for (i <- 1 to 30) {
      val now = DateTime.now().getMillis
      userRepo.insert(User(None, now, "Jan", "Kowalski" + i, 1, None, None, None, 0)).futureValue
    }

    userRepo.getUsers(0, 20).futureValue should have size 20
  }

  it should "get users with offset greater than 0" in {
    for (i <- 1 to 50) {
      val now = DateTime.now().getMillis
      userRepo.insert(User(None, now, "Jan", "Kowalski" + i, 1, None, None, None, 0)).futureValue
    }

    userRepo.getUsers(23, 20).futureValue.last.lastName should be("Kowalski43")
  }

  it should "update user" in {
    val user = User(None, DateTime.now().getMillis, "Jan", "Kowalski", 1, Some("Marszalkowska 12/123"), Some("12-345"), Some("Warsaw"), 0)
    val userId = userRepo.insert(user).futureValue.id.get

    val newUser = user.copy(
      id = Some(userId),
      firstName = "Kasia",
      lastName = "Nowak",
      gender = 0,
      streetAddress = Some("Krucza 12"),
      postalCode = Some("65-432"),
      postalCity = Some("Radom")
    )
    userRepo.update(userId, newUser.firstName, newUser.lastName, newUser.gender, newUser.streetAddress, newUser.postalCode, newUser.postalCity).futureValue

    userRepo.findByUserId(userId).futureValue should equal(Some(newUser))
  }

  it should "update user role" in {
    val user = User(None, DateTime.now().getMillis, "Jan", "Kowalski", 1, None, None, None, 0)
    val userId = userRepo.insert(user).futureValue.id.get

    userRepo.update(userId, 1).futureValue

    userRepo.findByUserId(userId).futureValue.map(_.role).get should be(1)
  }

  it should "delete user" in {
    val user = User(None, DateTime.now().getMillis, "Jan", "Kowalski", 1, None, None, None, 1)
    val userId = userRepo.insert(user).futureValue.id.get

    userRepo.delete(userId).futureValue

    userRepo.findByUserId(userId).futureValue should be(None)
  }
}
