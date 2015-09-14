package core.protocol

import core.dto.ErrorWrapper
import spray.json.DefaultJsonProtocol

/**
 * Created by piobab on 14.09.15.
 */
object CommonJsonProtocol extends DefaultJsonProtocol {
  implicit val errorWrapperFormat = jsonFormat3(ErrorWrapper)
}
