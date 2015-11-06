package core.authentication

case class Identity(authToken: String, user: Identity.User)

object Identity {

  case class User(id: Long, role: Int)

}
