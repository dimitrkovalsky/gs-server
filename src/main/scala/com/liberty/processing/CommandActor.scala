package com.liberty.processing

import akka.actor.{ActorRef, Actor}
import xitrum.Log
import com.liberty.validation.Validatable
import com.liberty.errors.Error
import com.codahale.jerkson.ParsingException
import com.liberty.annotation.RequestData
import com.liberty.model.GameSession
import com.liberty.requests.GenericRequest
import com.liberty.responses.GenericResponse

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 13:55
 */
trait CommandActor extends Actor with Log {
  var session: GameSession = _
  var requestId: Long = _
  var requester: ActorRef = _
  var genericRequest: GenericRequest = _


  override def receive: Actor.Receive = {

    case processing: ProcessingMessage =>
      setData(processing)
      try {
        this match {
          case validatable: Validatable => validatable.validate(processing.request)
        }

        handle()

      } catch {
        case ex: ParsingException => respondFail(Error.invalidCommandPacket)
        case thr: Throwable => respondFail(Error.exceptionOccurred(thr))
      }
    case any: Any => log.warn(s"[${this.getClass.getName}] received unexpected message : " + any)
  }

  private def processParsing(data: Any) {
    val clazz = getClass

    for (field <- clazz.getDeclaredFields) {
      if (field.isAnnotationPresent(classOf[RequestData])) {
        field.setAccessible(true)
        //  field.set(this, JsonMapper.convert[field.type](data, classOf[field.type]))
      }
    }
  }

  def setData(message: ProcessingMessage) {
    requestId = message.id
    session = message.session
    requester = sender
    genericRequest = message.request
  }

  def respondSuccess(response: GenericResponse) {
    requester ! ProcessingAck(requestId, response)
  }

  def respondSuccess() {
    respondSuccess(GenericResponse(genericRequest))
  }

  def handle()


  def respondFail(error: Error): Unit = {
    requester ! ProcessingError(requestId, error)
  }

  override def postStop() {
    super.postStop()
    log.debug(s"[$getClass] is closing onClose")
  }
}
