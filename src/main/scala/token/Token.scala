package token

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

case class Token(
                  value: String,
                  createdAt: Long,
                  valid: Boolean,
                  userId: Long
                  )

/**
 * Keeps information about all tokens used during authentication (this is tokens history).
 */
class Tokens(tag: Tag) extends Table[Token](tag, "token") {

  def value: Rep[String] = column[String]("value", O.PrimaryKey)

  def createdAt: Rep[Long] = column[Long]("created_at")

  def valid: Rep[Boolean] = column[Boolean]("valid")

  def userId: Rep[Long] = column[Long]("user_id")

  def * : ProvenShape[Token] = (value, createdAt, valid, userId) <>(Token.tupled, Token.unapply)
}