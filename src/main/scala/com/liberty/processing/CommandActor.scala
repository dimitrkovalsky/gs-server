package com.liberty.processing

import akka.actor.{ActorRef, Actor}
import xitrum.Log
import com.liberty.validation.Validatable
import com.liberty.errors.Error
import com.codahale.jerkson.ParsingException

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 13:55
 */
trait CommandActor extends Actor with Log {


  override def receive: Actor.Receive = {

    case processing: ProcessingMessage =>
      try {
        this match {
          case validatable: Validatable => validatable.validate(processing.request)
        }

        handle(processing)

      } catch {
        case ex: ParsingException => handleFail(processing, Error.invalidCommandPacket)
        case thr: Throwable => handleFail(processing, Error.exceptionOccurred(thr))
      }
    case any: Any => log.warn(s"[${this.getClass.getName}] received unexpected message : " + any)
  }


  def handleSuccess()

  def handle(message: ProcessingMessage)


  def handleFail(message: ProcessingMessage, error: Error): Unit = {
    sender ! ProcessingError(message.id, error)
  }
}
