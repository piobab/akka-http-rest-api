package core.authentication

import akka.http.scaladsl.model.headers.HttpChallenge
import akka.http.scaladsl.server.{AuthenticationFailedRejection, Directive, RequestContext}
import redis.RedisClient
import core.authentication.UserSerializer._

import scala.concurrent.ExecutionContext

trait TokenAuthenticator {

  val realm: String = "Private API"

  def authenticate(implicit ec: ExecutionContext, redisClient: RedisClient): Directive[Tuple1[Identity]] = Directive[Tuple1[Identity]] { inner => ctx =>
    getToken(ctx) match {
      case Some(token) =>
        redisClient.get[Identity.User](token).flatMap {
          case Some(value) =>
            inner(Tuple1(Identity(token, value)))(ctx)
          case None => ctx.reject(AuthenticationFailedRejection(AuthenticationFailedRejection.CredentialsRejected, createChallenge(ctx, Some("Invalid Auth-Token"))))
        }
      case None =>
        ctx.reject(AuthenticationFailedRejection(AuthenticationFailedRejection.CredentialsMissing, createChallenge(ctx, Some("Missing Auth-Token"))))
    }
  }

  private def getToken(ctx: RequestContext): Option[String] = ctx.request.headers.collectFirst {
    case pair if pair.name == "Auth-Token" => pair.value
  }

  private def createChallenge(ctx: RequestContext, message: Option[String]): HttpChallenge = {
    val params = message match {
      case Some(msg) => Map("error" -> "invalid_token", "error_description" -> msg)
      case None => Map.empty[String, String]
    }
    HttpChallenge(ctx.request.getUri().scheme(), realm, params)
  }
}
