package core.authentication

import java.util.UUID

/**
 * Created by piobab on 19.10.15.
 */
object Token {
  def generate: String = {
    UUID.randomUUID().toString
  }
}
