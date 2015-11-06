package utils

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import core.config.RedisConfig
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import redis.RedisClient

trait FlatSpecWithRedis extends FlatSpec with BeforeAndAfterAll with RedisConfig {

  override def config = ConfigFactory.load()

  implicit def actorSystem: ActorSystem

  implicit lazy val redis = RedisClient(host = redisAuthHost, port = redisAuthPort, password = Option(redisAuthPassword), db = Option(redisAuthDb))

  override protected def beforeAll() {
    super.beforeAll()
    redis.flushdb()
  }
}
