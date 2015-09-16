package user.repository

import slick.driver.PostgresDriver.api._
import user.be.{UserEntity, Users}
import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by piobab on 16.09.15.
 */
class UserRepository(implicit db: Database, ec: ExecutionContext) {

  val usersTQ = TableQuery[Users]

  def findByUserId(id: Long): Future[Option[UserEntity]] = db.run {
    byUserIdQueryCompiled(id).result.headOption
  }

  def insert(userEntity: UserEntity): Future[UserEntity] = {
    val userEntityWithId = (usersTQ returning usersTQ.map(_.id)
      into ((_, id) => userEntity.copy(id = id))) += userEntity

    db.run(userEntityWithId)
  }

  private def byUserIdQuery(id: Rep[Long]) = usersTQ.filter(_.id === id)

  private val byUserIdQueryCompiled = Compiled(byUserIdQuery _)
}
