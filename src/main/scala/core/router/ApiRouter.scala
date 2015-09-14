package core.router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import core.Config

/**
 * Created by piobab on 13.09.15.
 */
trait ApiRouter extends AuthenticationRouter with UserRouter {
  this: Config =>

  val apiRoutes: Route = {
    pathPrefix("api") {
      authenticationRoutes ~ userRoutes
    }
  }
}
