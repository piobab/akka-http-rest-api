package core.authorization

import akka.http.scaladsl.server.Directive

import scala.concurrent.ExecutionContext

/**
 * Created by piobab on 19.10.15.
 */
trait Permission {
  def authorize(check: () => Boolean)(implicit ec: ExecutionContext): Directive[Unit] = Directive[Unit] { inner => ctx =>
    if (check()) {
      inner()(ctx)
    } else {
      ctx.reject(PermissionRejection("User is not authorized to execute the request"))
    }
  }
}
