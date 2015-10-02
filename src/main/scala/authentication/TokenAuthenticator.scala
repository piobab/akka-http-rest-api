package authentication

import akka.http.scaladsl.model.headers.HttpChallenge
import akka.http.scaladsl.server.directives.{AuthenticationResult, AuthenticationDirective}
import akka.http.scaladsl.server.Directives._
import authentication.be.Identity

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by piobab on 01.10.15.
 */
trait TokenAuthenticator {

  val realm: String = "Private API"

  def authenticate(implicit ec: ExecutionContext): AuthenticationDirective[Identity] = {
    authenticateOrRejectWithChallenge[Identity] {
      case Some(httpCredentials) => httpCredentials.params.get("Auth-Token") match {
        case Some(authToken) => Future(grant(Identity("id", authToken)))
        case None => Future(deny(Some("Missing Auth-Token")))
      }
      case None => Future(deny(Some("No HTTP credentials")))
    }
  }

  private def grant(identity: Identity) = AuthenticationResult.success(identity)
  private def deny(message: Option[String]) = AuthenticationResult.failWithChallenge(createChallenge(message))

  private def createChallenge(message: Option[String]): HttpChallenge = {
    val params = message match {
      case Some(msg) => Map("error" -> "invalid_token", "error_description" -> msg)
      case None => Map.empty[String, String]
    }
    HttpChallenge("Basic", realm, params)
  }
}
