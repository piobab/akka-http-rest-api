package core.config

import com.typesafe.config.Config

trait ServerConfig {
  def config: Config

  lazy val serverInterface = config.getString("http.interface")
  lazy val serverPort = config.getInt("http.port")
}
