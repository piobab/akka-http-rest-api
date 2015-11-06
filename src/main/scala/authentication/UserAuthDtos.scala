package authentication

case class UserRegistrationRequest(email: String, password: String, firstName: String, lastName: String, gender: Int, role: Int)

case class UserLoginRequest(email: String, password: String)