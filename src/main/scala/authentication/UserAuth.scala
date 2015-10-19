package authentication

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

/**
 * Created by piobab on 14.09.15.
 */
case class UserAuth(
                     id: Option[Long],
                     email: String,
                     password: String,
                     userId: Long
                     )

class UsersAuth(tag: Tag) extends Table[UserAuth](tag, "user_auth") {

  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def email: Rep[String] = column[String]("email")

  def password: Rep[String] = column[String]("password")

  def userId: Rep[Long] = column[Long]("user_id")

  def * : ProvenShape[UserAuth] = (id.?, email, password, userId) <>(UserAuth.tupled, UserAuth.unapply)
}
