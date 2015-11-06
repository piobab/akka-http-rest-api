package user

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

case class User(
                 id: Option[Long],
                 createdAt: Long,
                 firstName: String,
                 lastName: String,
                 gender: Int,
                 streetAddress: Option[String],
                 postalCode: Option[String],
                 postalCity: Option[String],
                 role: Int = 0
                 )

class Users(tag: Tag) extends Table[User](tag, "user") {

  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def createdAt: Rep[Long] = column[Long]("created_at")

  def firstName: Rep[String] = column[String]("first_name")

  def lastName: Rep[String] = column[String]("last_name")

  def gender: Rep[Int] = column[Int]("gender")

  def streetAddress: Rep[Option[String]] = column[Option[String]]("street_address")

  def postalCode: Rep[Option[String]] = column[Option[String]]("postal_code")

  def postalCity: Rep[Option[String]] = column[Option[String]]("postal_city")

  def role: Rep[Int] = column[Int]("role")

  def * : ProvenShape[User] = (id.?, createdAt, firstName, lastName, gender, streetAddress, postalCode, postalCity, role) <>(User.tupled, User.unapply)
}