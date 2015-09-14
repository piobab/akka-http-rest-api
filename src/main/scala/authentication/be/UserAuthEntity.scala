package authentication.be

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

/**
 * Created by piobab on 14.09.15.
 */
case class UserAuthEntity(
                           id: Long,
                           createdAt: Long,
                           email: String,
                           password: String
                           )

class UsersAuth(tag: Tag) extends Table[UserAuthEntity](tag, "user_auth") {

  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def createdAt: Rep[Long] = column[Long]("created_at")

  def email: Rep[String] = column[String]("email")

  def password: Rep[String] = column[String]("password")

  def * : ProvenShape[UserAuthEntity] = (id, createdAt, email, password) <> (UserAuthEntity.tupled, UserAuthEntity.unapply)
}
