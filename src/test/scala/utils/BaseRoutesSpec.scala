package utils

import akka.event.NoLogging
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, FlatSpec}

trait BaseRoutesSpec extends FlatSpec with ScalatestRouteTest with Matchers { spec =>

  trait TestRoutesSupport {
    implicit val actorSystem = spec.system
    implicit val executor = spec.executor
    implicit val materializer = spec.materializer

    val logger = NoLogging
  }
}
