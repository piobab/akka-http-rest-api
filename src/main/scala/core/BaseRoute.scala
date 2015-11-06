package core

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.ActorMaterializer
import redis.RedisClient

import scala.concurrent.ExecutionContext

trait BaseRoute {
  implicit val actorSystem: ActorSystem
  implicit def executor: ExecutionContext
  implicit val materializer: ActorMaterializer

  val logger: LoggingAdapter

  implicit def redis: RedisClient
}
