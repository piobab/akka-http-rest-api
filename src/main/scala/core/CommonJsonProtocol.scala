package core

import spray.json.DefaultJsonProtocol

/**
 * Created by piobab on 14.09.15.
 */
object CommonJsonProtocol extends DefaultJsonProtocol {
  implicit val errorWrapperFormat = jsonFormat3(ErrorWrapper)
  implicit val statusWrapperFormat = jsonFormat2(StatusWrapper)
}
