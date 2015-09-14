package core.router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import core.Config

/**
 * Created by piobab on 13.09.15.
 */
trait UserRouter {
  this: Config =>

  val userRoutes: Route = {
    path("users" / Segment) { userId =>
      complete {
        s"OK $userId"
      }
    }
  }
}
