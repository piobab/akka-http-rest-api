package utils

import org.flywaydb.core.Flyway
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec}
import slick.driver.H2Driver.api._

trait FlatSpecWithSql extends FlatSpec with BeforeAndAfterAll with BeforeAndAfterEach with ScalaFutures {

  private val connectionString = "jdbc:h2:mem:rest_api_test" + this.getClass.getSimpleName + ";DB_CLOSE_DELAY=-1"
  implicit val db = Database.forURL(connectionString)

  override protected def beforeAll() {
    super.beforeAll()
    createAll()
  }

  private def createAll() {
    val flyway = new Flyway()
    flyway.setDataSource(connectionString, "", "")
    flyway.migrate()
  }

  override protected def afterAll() {
    super.afterAll()
    dropAll()
    db.close()
  }

  private def dropAll() {
    db.run(sqlu"DROP ALL OBJECTS").futureValue
  }

  override protected def afterEach() {
    try {
      clearData()
    }
    catch {
      case e: Exception => e.printStackTrace()
    }

    super.afterEach()
  }

  private def clearData() {
    dropAll()
    createAll()
  }
}
