package core.router

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import authentication.repository.UserAuthRepository
import authentication.{UserAuthService, UserLoginRequest, UserRegistrationRequest}
import core.Config
import user.repository.UserRepository

/**
 * Created by piobab on 13.09.15.
 */
trait AuthenticationRouter {
  this: Config =>

  import authentication.protocol.UserAuthJsonProtocol._

  // Lazy evaluation for db and execution context availability
  lazy val userAuthRepository = new UserAuthRepository
  lazy val userRepository = new UserRepository
  lazy val userAuthService = new UserAuthService(userAuthRepository, userRepository)

  val authenticationRoutes: Route = {
    pathPrefix("auth") {
      (post & pathPrefix("register") & pathEnd & entity(as[UserRegistrationRequest])) { request =>
        complete {
          userAuthService.register(request.email, request.password).map(_ => "OK")
        }
      } ~ (post & pathPrefix("login") & pathEnd & entity(as[UserLoginRequest])) { request =>
        complete {
          "OK"
        }
      }
    }
  }
}
