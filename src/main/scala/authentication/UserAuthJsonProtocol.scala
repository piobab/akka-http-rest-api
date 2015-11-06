package authentication

import spray.json.DefaultJsonProtocol

object UserAuthJsonProtocol extends DefaultJsonProtocol {
  implicit val userRegistrationRequestFormat = jsonFormat6(UserRegistrationRequest)
  implicit val userLoginRequestFormat = jsonFormat2(UserLoginRequest)
}
