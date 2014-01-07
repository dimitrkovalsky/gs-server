package com.liberty.errors

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 15:03
 */
case class Error(errorCode: Int, message: String) {}

object Error {
  val PROCESSING_FAILED = 301
  val AUTHENTICATION_FAILED = 302
  val UNHANDLED_MESSAGE = 303
  val INVALID_COMMAND_PACKET = 304
  val EXCEPTION_OCCURRED = 305
  val INVALID_SESSION = 306
  val TIMEOUT_ERROR = 307
  val VALIDATION_ERROR = 308
  val UNKNOWN_COMMAND = 310

  def authenticationFailed = Error(AUTHENTICATION_FAILED, "Authentication failed")

  def processingFailed = Error(PROCESSING_FAILED, "Request processing failed failed")

  def unhandledMessage(any: Any) = Error(UNHANDLED_MESSAGE, s"Unhandled message : $any")

  def unknownCommand = Error(UNKNOWN_COMMAND, "Unknown command")

  def invalidCommandPacket = Error(INVALID_COMMAND_PACKET, "Bad command packet")

  def exceptionOccurred(t: Throwable) = Error(EXCEPTION_OCCURRED, "Exception occurred : " + t)

  def invalidSession = Error(INVALID_SESSION, "Invalid session")

  def timeoutError = Error(TIMEOUT_ERROR, "Timeout exception")

  def validationError(message: String) = Error(VALIDATION_ERROR, message)
}

