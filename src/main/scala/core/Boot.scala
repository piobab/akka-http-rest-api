package core

import _root_.authentication.{UserAuthService, UserAuthRepository}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import redis.RedisClient
import slick.driver.PostgresDriver.api._
import user.{UserRepository, UserService}

/**
 * Created by piobab on 13.09.15.
 *
 * Mix with Config in order to override configuration data. This solution can be used for testing purpose.
 */
object Boot extends App with ApiRouter {
  override implicit val actorSystem = ActorSystem()
  override implicit val executor = actorSystem.dispatcher
  override implicit val materializer = ActorMaterializer()

  val dbUrl = CakeConfig.dbUrl
  val dbUser = CakeConfig.dbUser
  val dbPassword = CakeConfig.dbPassword
  override implicit val db = Database.forURL(url = dbUrl, user = dbUser, password = dbPassword, driver = "org.postgresql.Driver")

  val redisAuthHost = CakeConfig.redisAuthHost
  val redisAuthPort = CakeConfig.redisAuthPort
  val redisAuthPassword = CakeConfig.redisAuthPassword
  val redisAuthDb = CakeConfig.redisAuthDb
  override implicit val redis = RedisClient(host = redisAuthHost, port = redisAuthPort, password = Option(redisAuthPassword), db = Option(redisAuthDb))

  lazy val userAuthRepo = new UserAuthRepository
  lazy val userRepo = new UserRepository
  override lazy val userAuthService = new UserAuthService(userAuthRepo, userRepo)
  override lazy val userService = new UserService(userAuthRepo, userRepo)

  Http().bindAndHandle(apiRoutes, CakeConfig.interface, CakeConfig.port)
}
