package core

import _root_.authentication.AuthenticationRouter
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import user.UserRouter

/**
 * Created by piobab on 13.09.15.
 */
trait ApiRouter extends AuthenticationRouter with UserRouter {

  val apiRoutes: Route = {
    pathPrefix("api") {
      authenticationRoutes ~ userRoutes
    }
  }
}
