package user.be

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

/**
 * Created by piobab on 16.09.15.
 */
case class UserEntity(
                       id: Long,
                       firstName: Option[String],
                       lastName: Option[String],
                       gender: Option[Int],
                       streetAddress: Option[String],
                       postalCode: Option[String],
                       postalCity: Option[String],
                       userAuthId: Long
                       )

class Users(tag: Tag) extends Table[UserEntity](tag, "user") {

  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def firstName: Rep[Option[String]] = column[Option[String]]("first_name")

  def lastName: Rep[Option[String]] = column[Option[String]]("last_name")

  def gender: Rep[Option[Int]] = column[Option[Int]]("gender")

  def streetAddress: Rep[Option[String]] = column[Option[String]]("street_address")

  def postalCode: Rep[Option[String]] = column[Option[String]]("postal_code")

  def postalCity: Rep[Option[String]] = column[Option[String]]("postal_city")

  def userAuthId: Rep[Long] = column[Long]("user_auth_id")

  def * : ProvenShape[UserEntity] = (id, firstName, lastName, gender, streetAddress, postalCode, postalCity,
    userAuthId) <>(UserEntity.tupled, UserEntity.unapply)
}