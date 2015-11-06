package user

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import authentication.{UserAuthResult, UserRegistrationRequest, UserAuthService, UserAuthRepository}
import core.ErrorWrapper
import org.scalatest.time.{Millis, Seconds, Span}
import token.TokenRepository
import utils.{FlatSpecWithRedis, FlatSpecWithSql, BaseRoutesSpec}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import user.UserJsonProtocol._
import core.CommonJsonProtocol._

class UserRouterSpec extends BaseRoutesSpec with FlatSpecWithSql with FlatSpecWithRedis {
  spec =>

  override implicit val actorSystem: ActorSystem = spec.system

  val tokenRepo = new TokenRepository
  val userRepo = new UserRepository
  val userAuthRepo = new UserAuthRepository
  val userAuthService = new UserAuthService(tokenRepo, userAuthRepo, userRepo)
  val userService = new UserService(tokenRepo, userAuthRepo, userRepo)

  val userRouter = new UserRouter with TestRoutesSupport {
    override val userService = spec.userService

    override implicit val redis = spec.redis
  }

  val routes = Route.seal(userRouter.userRoutes)

  // how long it should wait before declaring that the future has timed out
  implicit val defaultPatience = PatienceConfig(timeout = Span(2, Seconds), interval = Span(50, Millis))

  private def withLoggedInUser(email: String, password: String, role: Int = 0)(body: RequestTransformer => Unit) = {
    userAuthService.register(UserRegistrationRequest(email, password, "Jan", "Kowalski", 1, role)).futureValue match {
      case UserAuthResult.Success(token) => body(addHeader("Auth-Token", token))
      case _ => body(addHeader("Auth-Token", ""))
    }
  }

  "GET /me" should "get logged user data" in {
    withLoggedInUser("jan@gmail.com", "pass") { transform =>
      Get("/users/me") ~> transform ~> routes ~> check {
        status should be(StatusCodes.OK)
        entityAs[User].firstName should be("Jan")
      }
    }
  }

  "GET /me" should "result in an error when user is not authenticated" in {
    Get("/users/me") ~> routes ~> check {
      status should be(StatusCodes.Unauthorized)
    }
  }

  "PUT /me" should "update logged user data" in {
    withLoggedInUser("jan@gmail.com", "pass") { transform =>
      Put("/users/me", UserUpdateRequest("Marcin", "Nowak", 1, None, None, None)) ~> transform ~> routes ~> check {
        status should be(StatusCodes.OK)
      }
    }
  }

  "GET /" should "get users" in {
    registerRandomUsers(40)

    withLoggedInUser("jan@gmail.com", "pass") { transform =>
      Get("/users?offset=0&limit=10") ~> transform ~> routes ~> check {
        status should be(StatusCodes.OK)
        val usersResponse = entityAs[UsersResponse]
        usersResponse.count should be(10)
        usersResponse.users should have size 10
      }
    }
  }

  "GET /10" should "get user data with id=10" in {
    registerRandomUsers(40)

    withLoggedInUser("jan@gmail.com", "pass") { transform =>
      Get("/users/10") ~> transform ~> routes ~> check {
        status should be(StatusCodes.OK)
        entityAs[User].lastName should be("Kowalski10")
      }
    }
  }

  "GET /10 with invalid user id" should "return 404 with an error message" in {
    withLoggedInUser("jan@gmail.com", "pass") { transform =>
      Get("/users/10") ~> transform ~> routes ~> check {
        status should be(StatusCodes.NotFound)
        entityAs[ErrorWrapper].userMessage should be("User with id=10 does not exist")
      }
    }
  }

  "DELETE /10 with wrong user role" should "return 403 with an error message" in {
    registerRandomUsers(20)

    withLoggedInUser("jan@gmail.com", "pass", 0) { transform =>
      Delete("/users/10") ~> transform ~> routes ~> check {
        status should be(StatusCodes.Forbidden)
        entityAs[ErrorWrapper].userMessage should be("User is not authorized to execute the request")
      }
    }
  }

  "DELETE /10" should "delete user with id=10" in {
    registerRandomUsers(20)

    withLoggedInUser("jan@gmail.com", "pass", 1) { transform =>
      Delete("/users/10") ~> transform ~> routes ~> check {
        status should be(StatusCodes.OK)
      }
    }
  }

  "DELETE /10 with invalid user id" should "return 404 with an error message" in {
    withLoggedInUser("jan@gmail.com", "pass", 1) { transform =>
      Delete("/users/10") ~> transform ~> routes ~> check {
        status should be(StatusCodes.NotFound)
      }
    }
  }

  "PATCH /10/role" should "update user role with user id equal 10" in {
    registerRandomUsers(20)

    withLoggedInUser("jan@gmail.com", "pass", 1) { transform =>
      Patch("/users/10/role", UserRoleUpdateRequest(1)) ~> transform ~> routes ~> check {
        status should be(StatusCodes.OK)
      }
    }
  }

  "DELETE /10/role with invalid user id" should "return 404 with an error message" in {
    withLoggedInUser("jan@gmail.com", "pass", 1) { transform =>
      Patch("/users/10/role", UserRoleUpdateRequest(1)) ~> transform ~> routes ~> check {
        status should be(StatusCodes.NotFound)
      }
    }
  }

  private def registerRandomUsers(number: Int) {
    for (i <- 1 to number) {
      userAuthService.register(UserRegistrationRequest(s"jan.kowalski$i@gmail.com", "password", "Jan", s"Kowalski$i", 1, 0)).futureValue
    }
  }
}
