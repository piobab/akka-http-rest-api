package authentication.repository

import authentication.be.{UserAuthEntity, UsersAuth}
import slick.driver.PostgresDriver.api._
import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by piobab on 14.09.15.
 */
class UserAuthRepository(implicit db: Database, ec: ExecutionContext) {

  val usersAuthTQ = TableQuery[UsersAuth]

  def findByUserAuthId(id: Long): Future[Option[UserAuthEntity]] = db.run {
    byUserAuthIdQueryCompiled(id).result.headOption
  }

  def insert(userAuthEntity: UserAuthEntity): Future[UserAuthEntity] = {
    val userAuthEntityWithId = (usersAuthTQ returning usersAuthTQ.map(_.id)
      into ((_, id) => userAuthEntity.copy(id = id))) += userAuthEntity

    db.run(userAuthEntityWithId)
  }

  private def byUserAuthIdQuery(id: Rep[Long]) = usersAuthTQ.filter(_.id === id)

  private val byUserAuthIdQueryCompiled = Compiled(byUserAuthIdQuery _)
}
