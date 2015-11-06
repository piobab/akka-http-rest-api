package authentication

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import core.{StatusWrapper, ErrorWrapper, BaseRoute}
import akka.http.scaladsl.model.StatusCodes

trait AuthenticationRouter extends BaseRoute {

  import UserAuthJsonProtocol._
  import core.CommonJsonProtocol._

  def userAuthService: UserAuthService

  val authenticationRoutes: Route = {
    pathPrefix("auth") {
      (path("register") & pathEnd & post & entity(as[UserRegistrationRequest])) { request =>
        complete {
          userAuthService.register(request).map[ToResponseMarshallable] {
            case UserAuthResult.InvalidData(msg) => StatusCodes.BadRequest -> ErrorWrapper("invalidData", msg)
            case UserAuthResult.UserExists(msg) => StatusCodes.Conflict -> ErrorWrapper("userExists", msg)
            case UserAuthResult.Success(token) => StatusCodes.Created -> StatusWrapper("OK", Some(token))
          }
        }
      } ~ (path("login") & pathEnd & post & entity(as[UserLoginRequest])) { request =>
        complete {
          userAuthService.login(request).map[ToResponseMarshallable] {
            case UserAuthResult.InvalidData(msg) => StatusCodes.BadRequest -> ErrorWrapper("invalidData", msg)
            case UserAuthResult.UserNotExists(msg) => StatusCodes.BadRequest -> ErrorWrapper("userNotExists", msg)
            case UserAuthResult.Success(token) => StatusCodes.OK -> StatusWrapper("OK", Some(token))
          }
        }
      }
    }
  }
}
