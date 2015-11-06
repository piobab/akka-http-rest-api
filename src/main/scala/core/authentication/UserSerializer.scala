package core.authentication

import akka.util.ByteString
import redis.ByteStringFormatter

object UserSerializer {
  implicit val byteStringFormatter = new ByteStringFormatter[Identity.User] {
    def serialize(data: Identity.User): ByteString = {
      ByteString(
        data.id + "|" + data.role
      )
    }

    def deserialize(bs: ByteString): Identity.User = {
      val r = bs.utf8String.split('|').toList
      Identity.User(r(0).toLong, r(1).toInt)
    }
  }
}
