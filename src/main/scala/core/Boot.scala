package core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

/**
 * Created by piobab on 13.09.15.
 */
object Boot extends App with ApiRouter {
  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = actorSystem.dispatcher

  val cakeConfig = CakeConfig

  Http().bindAndHandle(apiRoutes, cakeConfig.interface, cakeConfig.port)
}
