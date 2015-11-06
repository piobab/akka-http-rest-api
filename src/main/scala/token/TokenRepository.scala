package token

import org.joda.time.DateTime
import slick.driver.PostgresDriver.api._

import scala.concurrent.{Future, ExecutionContext}

class TokenRepository(implicit db: Database, ec: ExecutionContext) {

  val tokensTQ = TableQuery[Tokens]

  def findByTokenValue(tokenValue: String): Future[Option[Token]] = {
    db.run(byTokenValueQueryCompiled(tokenValue).result.headOption)
  }

  def getValidTokens(userId: Long): Future[Seq[Token]] = {
    db.run(byUserIdAndValidityQueryCompiled(userId, true).result)
  }

  def insert(tokenId: String, userId: Long): Future[Token] = {
    val token = Token(tokenId, DateTime.now().getMillis, true, userId)
    db.run(tokensTQ += token).map(_ => token)
  }

  def update(tokenValue: String, valid: Boolean): Future[Unit] = {
    db.run(tokensTQ.filter(_.value === tokenValue).map(t => t.valid).update(valid)).map(_ => ())
  }

  def update(userId: Long, valid: Boolean): Future[Unit] = {
    db.run(tokensTQ.filter(_.userId === userId).map(t => t.valid).update(valid)).map(_ => ())
  }

  private def byTokenValueQuery(tokenValue: Rep[String]) = tokensTQ.filter(_.value === tokenValue)

  private def byUserIdAndValidityQuery(userId: Rep[Long], valid: Rep[Boolean]) =
    tokensTQ.filter(t => t.userId === userId && t.valid === valid)

  private val byTokenValueQueryCompiled = Compiled(byTokenValueQuery _)

  private val byUserIdAndValidityQueryCompiled = Compiled(byUserIdAndValidityQuery _)
}
