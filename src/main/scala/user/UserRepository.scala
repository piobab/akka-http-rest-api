package user

import slick.driver.PostgresDriver.api._

import scala.concurrent.{ExecutionContext, Future}

class UserRepository(implicit db: Database, ec: ExecutionContext) {

  val usersTQ = TableQuery[Users]

  def findByUserId(id: Long): Future[Option[User]] = {
    db.run(byUserIdQueryCompiled(id).result.headOption)
  }

  def getUsers(offset: Long, limit: Int): Future[Seq[User]] = {
    db.run(usersTQ.sortBy(u => u.id.asc).drop(offset).take(limit).result)
  }

  def insert(user: User): Future[User] = {
    val userWithId = (usersTQ returning usersTQ.map(_.id)
      into ((_, id) => user.copy(id = Some(id)))) += user

    db.run(userWithId)
  }

  def update(id: Long,
             firstName: String,
             lastName: String,
             gender: Int,
             streetAddress: Option[String],
             postalCode: Option[String],
             postalCity: Option[String]): Future[Unit] = {
    db.run(usersTQ
      .filter(_.id === id)
      .map(user => (user.firstName, user.lastName, user.gender, user.streetAddress, user.postalCode, user.postalCity))
      .update(firstName, lastName, gender, streetAddress, postalCode, postalCity))
      .map(_ => ())
  }

  def update(id: Long, role: Int): Future[Unit] = {
    db.run(usersTQ.filter(_.id === id).map(u => u.role).update(role)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = {
    db.run(byUserIdQueryCompiled(id).delete).map(_ => ())
  }

  private def byUserIdQuery(id: Rep[Long]) = usersTQ.filter(_.id === id)

  private val byUserIdQueryCompiled = Compiled(byUserIdQuery _)
}
