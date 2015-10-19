package core.authentication

import user.User

/**
 * Created by piobab on 01.10.15.
 */
case class Identity(authToken: String, user: User)
