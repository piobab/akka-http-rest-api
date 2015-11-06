package user

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import core.authorization.{WithPermissionRejections, Permission}
import core.{ErrorWrapper, StatusWrapper, BaseRoute}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import core.authentication.{Identity, TokenAuthenticator}

trait UserRouter extends TokenAuthenticator with Permission with WithPermissionRejections with BaseRoute {

  import UserJsonProtocol._
  import core.CommonJsonProtocol._

  def userService: UserService

  val userRoutes: Route = {
    pathPrefix("users") {
      authenticate(executor, redis) { identity =>
        (path("me") & pathEnd & get) {
          complete {
            userService.getUser(identity.user.id).map[ToResponseMarshallable] {
              case Left(msg) => StatusCodes.NotFound -> ErrorWrapper("invalidData", msg)
              case Right(user) => StatusCodes.OK -> user
            }
          }
        } ~ (path("me") & pathEnd & put & entity(as[UserUpdateRequest])) { request =>
          complete {
            userService.updateLoggedUser(identity, request).map[ToResponseMarshallable] { _ =>
              StatusCodes.OK -> StatusWrapper()
            }
          }
        } ~ (pathEnd & get & parameters('offset.as[Long], 'limit.as[Int] ? 20)) { (offset, limit) =>
          complete {
            userService.getUsers(offset, limit).map[ToResponseMarshallable] { usersResponse =>
              StatusCodes.OK -> usersResponse
            }
          }
        } ~ (path(LongNumber) & pathEnd & get) { id =>
          complete {
            userService.getUser(id).map[ToResponseMarshallable] {
              case Left(msg) => StatusCodes.NotFound -> ErrorWrapper("invalidData", msg)
              case Right(user) => StatusCodes.OK -> user
            }
          }
        } ~ (path(LongNumber) & pathEnd & delete & handleRejections(permissionRejectionHandlers) & authorize(hasPermission(identity))) { id =>
          complete {
            userService.deleteUser(id).map[ToResponseMarshallable] {
              case Left(msg) => StatusCodes.NotFound -> ErrorWrapper("invalidData", msg)
              case Right(_) => StatusCodes.OK -> StatusWrapper()
            }
          }
        } ~ (path(LongNumber / "role") & pathEnd & patch & handleRejections(permissionRejectionHandlers) & authorize(hasPermission(identity)) & entity(as[UserRoleUpdateRequest])) {
          (id, request) =>
            complete {
              userService.updateRole(id, identity, request).map[ToResponseMarshallable] {
                case Left(msg) => StatusCodes.NotFound -> ErrorWrapper("invalidData", msg)
                case Right(_) => StatusCodes.OK -> StatusWrapper()
              }
            }
        }
      }
    }
  }

  def hasPermission(identity: Identity): () => Boolean = () => identity.user.role == 1
}
