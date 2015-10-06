package core

import com.typesafe.config.ConfigFactory

/**
 * Created by piobab on 13.09.15.
 */
object CakeConfig {
  lazy val config = ConfigFactory.load()
  lazy val interface = config.getString("http.interface")
  lazy val port = config.getInt("http.port")

  lazy val dbUrl = config.getString("db.url")
  lazy val dbUser = config.getString("db.user")
  lazy val dbPassword = config.getString("db.password")

  lazy val redisAuthHost = config.getString("redis.auth.host")
  lazy val redisAuthPort = config.getInt("redis.auth.port")
  lazy val redisAuthPassword = config.getString("redis.auth.password")
  lazy val redisAuthDb = config.getInt("redis.auth.db")

  lazy val redisCacheHost = config.getString("redis.cache.host")
  lazy val redisCachePort = config.getInt("redis.cache.port")
  lazy val redisCachePassword = config.getString("redis.cache.password")
  lazy val redisCacheDb = config.getInt("redis.cache.db")
}
