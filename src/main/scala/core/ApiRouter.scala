package core

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

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
