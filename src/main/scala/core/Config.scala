package core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext

/**
 * Created by piobab on 14.09.15.
 */
trait Config {
  implicit val actorSystem: ActorSystem
  implicit def executor: ExecutionContext
  implicit val materializer: ActorMaterializer

  implicit def db: Database
}
