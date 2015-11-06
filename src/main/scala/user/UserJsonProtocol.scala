package user

import spray.json.DefaultJsonProtocol

object UserJsonProtocol extends DefaultJsonProtocol {
  implicit val userUpdateRequestFormat = jsonFormat6(UserUpdateRequest)
  implicit val userFormat = jsonFormat9(User)
  implicit val usersResponseFormat = jsonFormat2(UsersResponse)
  implicit val userRoleUpdateRequestFormat = jsonFormat1(UserRoleUpdateRequest)
}
