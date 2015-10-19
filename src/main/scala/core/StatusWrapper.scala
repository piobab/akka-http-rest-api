package core

case class StatusWrapper(status: String = "OK", token: Option[String] = None)
