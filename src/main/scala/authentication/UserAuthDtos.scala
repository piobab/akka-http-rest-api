package authentication

/**
 * Created by piobab on 14.09.15.
 */
case class UserRegistrationRequest(email: String, password: String)

case class UserLoginRequest(email: String, password: String)