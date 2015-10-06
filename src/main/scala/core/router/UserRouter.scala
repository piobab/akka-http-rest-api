package core.router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import authentication.TokenAuthenticator
import core.Config

/**
 * Created by piobab on 13.09.15.
 */
trait UserRouter extends TokenAuthenticator {
  this: Config =>

  val userRoutes: Route = {
    pathPrefix("users") {
      (get & pathPrefix("me") & pathEnd) {
        authenticate(executor, redis) { identity =>
          complete(s"OK, token: ${identity.authToken}, id: ${identity.id}")
        }
      }
    }
  }
}
