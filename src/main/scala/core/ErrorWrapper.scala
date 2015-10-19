package core

case class ErrorWrapper(code: String, userMessage: String, exceptionMessage: Option[String] = None)

