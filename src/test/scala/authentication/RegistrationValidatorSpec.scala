package authentication

import org.scalatest.{Matchers, FlatSpec}

class RegistrationValidatorSpec extends FlatSpec with Matchers {

  "isDataValid()" should "accept valid data" in {
    val dataIsValid: Boolean = RegistrationValidator.isDataValid("admin@gmail.com", "password")

    dataIsValid should be(true)
  }

  "isDataValid()" should "not accept missing email with spaces only" in {
    val dataIsValid: Boolean = RegistrationValidator.isDataValid("   ", "password")

    dataIsValid should be(false)
  }

  "isDataValid()" should "not accept invalid email" in {
    val dataIsValid: Boolean = RegistrationValidator.isDataValid("invalidEmail", "password")

    dataIsValid should be(false)
  }

  "isDataValid()" should "not accept password with empty spaces only" in {
    val dataIsValid: Boolean = RegistrationValidator.isDataValid("admin@gmail.com", "    ")

    dataIsValid should be(false)
  }
}
