package user

case class UserUpdateRequest(firstName: String,
                             lastName: String,
                             gender: Int,
                             streetAddress: Option[String],
                             postalCode: Option[String],
                             postalCity: Option[String])

case class UsersResponse(count: Long, users: Seq[User])

case class UserRoleUpdateRequest(role: Int)