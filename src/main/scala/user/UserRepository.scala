package user

import slick.driver.PostgresDriver.api._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by piobab on 16.09.15.
 */
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

  def update(user: User): Future[Unit] = {
    db.run(usersTQ.filter(_.id === user.id).update(user)).map(_ => ())
  }

  def update(id: Long, admin: Int): Future[Unit] = {
    db.run(usersTQ.filter(_.id === id).map(u => u.admin).update(admin)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = {
    db.run(byUserIdQueryCompiled(id).delete).map(_ => ())
  }

  private def byUserIdQuery(id: Rep[Long]) = usersTQ.filter(_.id === id)

  private val byUserIdQueryCompiled = Compiled(byUserIdQuery _)
}
