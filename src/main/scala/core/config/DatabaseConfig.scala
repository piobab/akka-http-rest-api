package core.config

import com.typesafe.config.Config

trait DatabaseConfig {
  def config: Config

  lazy val dbUrl = config.getString("db.url")
  lazy val dbUser = config.getString("db.user")
  lazy val dbPassword = config.getString("db.password")
}
