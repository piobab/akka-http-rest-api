package user

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

/**
 * Created by piobab on 16.09.15.
 */
case class User(
                 id: Option[Long],
                 createdAt: Long,
                 firstName: String,
                 lastName: String,
                 gender: Int,
                 streetAddress: Option[String],
                 postalCode: Option[String],
                 postalCity: Option[String],
                 admin: Int = 0
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

  def admin: Rep[Int] = column[Int]("admin")

  def * : ProvenShape[User] = (id.?, createdAt, firstName, lastName, gender, streetAddress, postalCode, postalCity, admin) <>(User.tupled, User.unapply)
}