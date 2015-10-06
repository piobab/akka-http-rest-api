package core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import core.router.ApiRouter
import redis.RedisClient
import slick.driver.PostgresDriver.api._

/**
 * Created by piobab on 13.09.15.
 *
 * Mix with Config in order to override configuration data. This solution can be used for testing purpose.
 */
object Boot extends App with ApiRouter with Config {
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

  Http().bindAndHandle(apiRoutes, CakeConfig.interface, CakeConfig.port)
}
