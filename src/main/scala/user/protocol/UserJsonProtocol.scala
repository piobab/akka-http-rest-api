package user.protocol

import spray.json.DefaultJsonProtocol
import user.UserRequest

/**
 * Created by piobab on 16.09.15.
 */
object UserJsonProtocol extends DefaultJsonProtocol {
  implicit val userRegistrationRequestFormat = jsonFormat2(UserRequest)
}
