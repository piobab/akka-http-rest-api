package authentication

import akka.actor.ActorSystem
import authentication.UserAuthResult.Success
import org.scalatest.Matchers
import org.scalatest.time.{Millis, Seconds, Span}
import token.TokenRepository
import user.UserRepository
import utils.{FlatSpecWithRedis, FlatSpecWithSql}

import scala.concurrent.ExecutionContext.Implicits.global

class UserAuthServiceSpec extends FlatSpecWithSql with FlatSpecWithRedis with Matchers {
  behavior of "UserAuthService"

  override implicit val actorSystem: ActorSystem = ActorSystem()

  val tokenRepo = new TokenRepository
  val userRepo = new UserRepository
  val userAuthRepo = new UserAuthRepository
  val userAuthService = new UserAuthService(tokenRepo, userAuthRepo, userRepo)

  implicit val defaultPatience = PatienceConfig(timeout = Span(2, Seconds), interval = Span(50, Millis))

  it should "register new user" in {
    val userRegistrationRequest = UserRegistrationRequest("jan.kowalski@gmail.com", "password123", "Jan", "Kowalski", 1, 1)

    userAuthService.register(userRegistrationRequest).futureValue should be(Success)
  }
}
