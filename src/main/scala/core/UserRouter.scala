package core

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

/**
 * Created by piobab on 13.09.15.
 */
trait UserRouter {
  val userRoutes: Route = {
    path("users" / Segment) { userId =>
      complete {
        s"OK $userId"
      }
    }
  }
}
