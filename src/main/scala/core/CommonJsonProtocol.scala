package core

import spray.json.DefaultJsonProtocol

object CommonJsonProtocol extends DefaultJsonProtocol {
  implicit val errorWrapperFormat = jsonFormat3(ErrorWrapper)
  implicit val statusWrapperFormat = jsonFormat2(StatusWrapper)
}
