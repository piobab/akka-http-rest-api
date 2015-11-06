package authentication

import slick.driver.PostgresDriver.api._

import scala.concurrent.{ExecutionContext, Future}

class UserAuthRepository(implicit db: Database, ec: ExecutionContext) {

  val usersAuthTQ = TableQuery[UsersAuth]

  def findByUserAuthId(id: Long): Future[Option[UserAuth]] = {
    db.run(byUserAuthIdQueryCompiled(id).result.headOption)
  }

  def findByUserId(id: Long): Future[Option[UserAuth]] = {
    db.run(byUserIdQueryCompiled(id).result.headOption)
  }

  def findByUserEmail(email: String): Future[Option[UserAuth]] = {
    db.run(byUserEmailQueryCompiled(email).result.headOption)
  }

  def insert(userAuth: UserAuth): Future[UserAuth] = {
    val userAuthWithId = (usersAuthTQ returning usersAuthTQ.map(_.id)
      into ((_, id) => userAuth.copy(id = Some(id)))) += userAuth

    db.run(userAuthWithId)
  }

  def deleteByUserId(id: Long): Future[Unit] = {
    db.run(byUserIdQueryCompiled(id).delete).map(_ => ())
  }

  private def byUserAuthIdQuery(id: Rep[Long]) = usersAuthTQ.filter(_.id === id)

  private def byUserIdQuery(id: Rep[Long]) = usersAuthTQ.filter(_.userId === id)

  private def byUserEmailQuery(email: Rep[String]) = usersAuthTQ.filter(_.email === email)

  private val byUserAuthIdQueryCompiled = Compiled(byUserAuthIdQuery _)

  private val byUserIdQueryCompiled = Compiled(byUserIdQuery _)

  private val byUserEmailQueryCompiled = Compiled(byUserEmailQuery _)
}
