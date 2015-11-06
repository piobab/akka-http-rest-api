package core

import _root_.authentication.{UserAuthService, UserAuthRepository}
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import core.config.{RedisConfig, DatabaseConfig, ServerConfig}
import redis.RedisClient
import slick.driver.PostgresDriver.api._
import token.TokenRepository
import user.{UserRepository, UserService}

object Boot extends App with ApiRouter with ServerConfig with DatabaseConfig with RedisConfig {

  override val config = ConfigFactory.load()

  override implicit val actorSystem = ActorSystem("rest-api-app")
  override implicit val executor = actorSystem.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val logger = Logging(actorSystem, getClass)

  override implicit val redis = RedisClient(host = redisAuthHost, port = redisAuthPort, password = Option(redisAuthPassword), db = Option(redisAuthDb))

  implicit val db = Database.forURL(url = dbUrl, user = dbUser, password = dbPassword, driver = "org.postgresql.Driver")

  lazy val tokenRepo = new TokenRepository
  lazy val userAuthRepo = new UserAuthRepository
  lazy val userRepo = new UserRepository
  override lazy val userAuthService = new UserAuthService(tokenRepo, userAuthRepo, userRepo)
  override lazy val userService = new UserService(tokenRepo, userAuthRepo, userRepo)

  Http().bindAndHandle(apiRoutes, serverInterface, serverPort)
}
