package core

import _root_.authentication.AuthenticationRouter
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import user.UserRouter

trait ApiRouter extends AuthenticationRouter with UserRouter {

  val apiRoutes: Route = {
    pathPrefix("api") {
      authenticationRoutes ~ userRoutes
    }
  }
}
