package core

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
 * Created by piobab on 13.09.15.
 */
trait AuthenticationRouter {
  val authenticationRoutes: Route = {
    path("register" / "password") {
      complete {
        "OK"
      }
    }
  }
}
