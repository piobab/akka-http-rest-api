package core.authorization

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{RejectionHandler, Rejection}
import core.ErrorWrapper
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import core.CommonJsonProtocol._

case class PermissionRejection(message: String) extends Rejection

trait WithPermissionRejections {
  implicit def permissionRejectionHandlers =
    RejectionHandler.newBuilder()
      .handle { case PermissionRejection(msg) ⇒
      complete(Forbidden, ErrorWrapper("forbidden", msg)) }
      .result()
}
