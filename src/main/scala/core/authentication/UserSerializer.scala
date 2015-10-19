package core.authentication

import akka.util.ByteString
import redis.ByteStringFormatter
import user.User

/**
 * Created by piobab on 17.10.15.
 */
object UserSerializer {
  implicit val byteStringFormatter = new ByteStringFormatter[User] {
    def serialize(data: User): ByteString = {
      ByteString(
        data.id.getOrElse(-1)
          + "|" + data.createdAt
          + "|" + data.firstName
          + "|" + data.lastName
          + "|" + data.gender
          + "|" + data.streetAddress.getOrElse("")
          + "|" + data.postalCode.getOrElse("")
          + "|" + data.postalCity.getOrElse("")
          + "|" + data.admin
      )
    }

    def deserialize(bs: ByteString): User = {
      val r = bs.utf8String.split('|').toList

      User(
        toOptionLong(r(0)),
        r(1).toLong,
        r(2),
        r(3),
        r(4).toInt,
        toOptionString(r(5)),
        toOptionString(r(6)),
        toOptionString(r(7)),
        r(8).toInt
      )
    }

    def toOptionLong(str: String): Option[Long] = {
      val id = str.toLong
      if (id >= 0) Some(id) else None
    }

    def toOptionString(str: String): Option[String] = {
      if (!str.isEmpty) Some(str) else None
    }
  }
}
