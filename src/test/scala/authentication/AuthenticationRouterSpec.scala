package authentication

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import core.ErrorWrapper
import token.TokenRepository
import user.UserRepository
import utils.{FlatSpecWithRedis, FlatSpecWithSql, BaseRoutesSpec}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import UserAuthJsonProtocol._
import core.CommonJsonProtocol._
import org.scalatest.time.{Millis, Seconds, Span}

class AuthenticationRouterSpec extends BaseRoutesSpec with FlatSpecWithSql with FlatSpecWithRedis {
  spec =>

  override implicit val actorSystem: ActorSystem = spec.system

  val tokenRepo = new TokenRepository
  val userRepo = new UserRepository
  val userAuthRepo = new UserAuthRepository
  val userAuthService = new UserAuthService(tokenRepo, userAuthRepo, userRepo)

  val authRouter = new AuthenticationRouter with TestRoutesSupport {
    override val userAuthService = spec.userAuthService

    override implicit val redis = spec.redis
  }

  val routes = Route.seal(authRouter.authenticationRoutes)

  // how long it should wait before declaring that the future has timed out
  implicit val defaultPatience = PatienceConfig(timeout = Span(2, Seconds), interval = Span(50, Millis))

  "POST /register" should "register new user" in {
    Post("/auth/register", UserRegistrationRequest("jan.kowalski@gmail.com", "password", "Jan", "Kowalski", 1, 0)) ~> routes ~> check {
      userAuthRepo.findByUserEmail("jan.kowalski@gmail.com").futureValue should be('defined)
      status should be(StatusCodes.Created)
    }
  }

  "POST /register with invalid data" should "result in an error" in {
    Post("/auth/register") ~> routes ~> check {
      status should be(StatusCodes.BadRequest)
    }
  }

  "POST /register with an existing email" should "return 409 with an error message" in {
    userAuthService.register(UserRegistrationRequest("jan.kowalski@gmail.com", "password", "Jan", "Kowalski", 1, 0)).futureValue

    Post("/auth/register", UserRegistrationRequest("jan.kowalski@gmail.com", "password", "Jan", "Kowalski", 1, 0)) ~> routes ~> check {
      status should be(StatusCodes.Conflict)
      entityAs[ErrorWrapper].userMessage should be("E-mail already in use")
    }
  }

  "POST /login with valid data" should "result in login user" in {
    userAuthService.register(UserRegistrationRequest("jan.kowalski@gmail.com", "password", "Jan", "Kowalski", 1, 0)).futureValue

    Post("/auth/login", UserLoginRequest("jan.kowalski@gmail.com", "password")) ~> routes ~> check {
      status should be(StatusCodes.OK)
    }
  }

  "POST /login with invalid data" should "result in an error" in {
    Post("/auth/login") ~> routes ~> check {
      status should be(StatusCodes.BadRequest)
    }
  }

  "POST /login with invalid password" should "result in an error" in {
    userAuthService.register(UserRegistrationRequest("jan.kowalski@gmail.com", "password", "Jan", "Kowalski", 1, 0)).futureValue

    Post("/auth/login", UserLoginRequest("jan.kowalski@gmail.com", "pass")) ~> routes ~> check {
      status should be(StatusCodes.BadRequest)
      entityAs[ErrorWrapper].userMessage should be("Password is invalid")
    }
  }

  "POST /login with unknown email" should "result in an error" in {
    Post("/auth/login", UserLoginRequest("invalidEmail@gmail.com", "password")) ~> routes ~> check {
      status should be(StatusCodes.BadRequest)
      entityAs[ErrorWrapper].userMessage should be("E-mail does not exist")
    }
  }

  "POST /auth/whatever" should "not be bound to /auth - reject unmatchedPath request" in {
    Post("/auth/whatever") ~> routes ~> check {
      status should be(StatusCodes.NotFound)
    }
  }
}
