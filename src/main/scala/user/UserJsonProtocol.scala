package user

import spray.json.DefaultJsonProtocol

/**
 * Created by piobab on 16.09.15.
 */
object UserJsonProtocol extends DefaultJsonProtocol {
  implicit val userUpdateRequestFormat = jsonFormat6(UserUpdateRequest)
  implicit val userFormat = jsonFormat9(User)
  implicit val usersResponseFormat = jsonFormat2(UsersResponse)
  implicit val userAdminUpdateRequestFormat = jsonFormat1(UserAdminUpdateRequest)
}
