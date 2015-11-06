package core.config

import com.typesafe.config.Config

trait RedisConfig {
  def config: Config

  lazy val redisAuthHost = config.getString("redis.auth.host")
  lazy val redisAuthPort = config.getInt("redis.auth.port")
  lazy val redisAuthPassword = config.getString("redis.auth.password")
  lazy val redisAuthDb = config.getInt("redis.auth.db")
}
