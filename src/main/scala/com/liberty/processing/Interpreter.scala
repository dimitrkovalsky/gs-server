package com.liberty.processing

import akka.actor.{Props, ActorRef, Actor}
import com.liberty.errors.Error
import com.codahale.jerkson.ParsingException
import com.liberty.validation.PacketValidator
import scala.concurrent.duration._
import com.liberty.responses.GenericResponse
import xitrum.Log
import xitrum.scope.session.Session
import com.liberty.helpers.Mapper._
import com.liberty.responses.Authenticated
import com.liberty.requests.GenericRequest
import scala.Some
import com.liberty.model.GameSession
import org.bson.types.ObjectId
import com.liberty.helpers.JsonMapper

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 14:57
 */

class Interpreter(front: ActorRef, session: Session) extends Actor with PacketValidator with Log {
  implicit val execution = context.system.dispatcher
  var processing = Map.empty[Long, OnProcessing]
  var permanents: List[ActorRef] = Nil

  var _idCounter = 0L

  def nextId = {
    val ret = _idCounter
    _idCounter += 1
    ret
  }


  def receive: Actor.Receive = {
    case raw: RawMessage => processMessage(raw)

    case auth: AuthenticatedAck => handleAuth(auth)

    case permanent: PermanentActorAck => handlePermanent(permanent)

    case ack: ProcessingAck => handleSuccess(ack)

    case error: ProcessingError => handleError(error)

    case ProcessingTimeout(id) => handleTimeout(id)

    case any: Any => println("[Interpreter] unhandled message : " + any)
      context.parent ! Error.unhandledMessage(any)
  }

  def respondError(err: Error, time: Long) {
    front ! ProcessingCompleted(GenericResponse.errorResponse(err), time)
  }

  def processMessage(message: RawMessage) {
    try {
      if (!validateBase(message)) {
        respondError(Error.invalidCommandPacket, message.startProcessingTime)
        return
      }
      val request = JsonMapper.parseRequest(message.data)
      val gameSession = getSession

      if (!validateAuthentication(request))
        respondError(Error.authenticationFailed, message.startProcessingTime)

      else if (!validateSession(request, gameSession))
        respondError(Error.invalidSession, message.startProcessingTime)

      else
        getHandler(request.requestType).map {
          sendMessage(_, createMessage(request, gameSession))
        }.getOrElse(respondError(Error.unknownCommand, message.startProcessingTime))

    } catch {
      case _: ParsingException => respondError(Error.invalidCommandPacket, message.startProcessingTime)
      case t: Throwable => respondError(Error.exceptionOccurred(t), message.startProcessingTime)
    }
  }

  override def postStop() {
    super.postStop()
    log.debug("[Interpreter] onClose")
    processing.values.foreach(v => context.stop(v.processor))
    permanents.foreach(context.stop)
  }

  def sendMessage(ref: ActorRef, msg: ProcessingMessage) {
    processing += msg.id -> OnProcessing(msg.id, System.currentTimeMillis(), ref)
    ref ! msg
    context.system.scheduler.scheduleOnce(2.seconds, self, ProcessingTimeout(msg.id))
  }

  def handleTimeout(id: Long) {
    processing.get(id).foreach {
      prc => log.error("[Interpreter] Timeout retrieved for id : " + id)
        processing -= id
        context.stop(prc.processor)
        respondError(Error.timeoutError, prc.startTime)
    }
  }

  def handleSuccess(ack: ProcessingAck) {
    handleSuccess(ack.id, ack.result)
  }

  def handleSuccess(id: Long, result: GenericResponse) {
    processing.get(id).foreach {
      prc =>
        processing -= id
        context.stop(prc.processor)
        front ! ProcessingCompleted(result, prc.startTime)
    }
  }

  def handleSuccess(id: Long)(handler: OnProcessing => Unit) {
    processing.get(id).foreach {
      prc =>
        processing -= id
        handler(prc)
    }
  }

  def handleAuth(authAck: AuthenticatedAck) {
    handleSuccess(authAck.id, GenericResponse.authenticatedResponse(authAck.auth))
  }

  def handlePermanent(ack: PermanentActorAck) {
    processing.get(ack.id).foreach {
      prc =>
        processing -= ack.id
        permanents = permanents :+ prc.processor
    }
  }

  def handleError(error: ProcessingError) {
    processing.get(error.id).foreach {
      prc =>
        processing -= error.id
        context.stop(prc.processor)
        respondError(error.error, prc.startTime)
    }
  }

  def createMessage(request: GenericRequest, session: Option[GameSession]) = {
    session.map {
      s => ProcessingMessage(nextId, sender, request, s)
    } getOrElse {
      ProcessingMessage(nextId, sender, request, null)
    }
  }

  def getHandler(requestType: Int): Option[ActorRef] = {
    CommandRegistry.getCommandClass(requestType).flatMap {
      commandActor: Class[_ <: CommandActor] => Some(context.actorOf(Props(commandActor)))
    }
  }

  def setSession(authenticated: Authenticated) {
    session += INTERNAL_ID -> authenticated.internalId
    session += EXTERNAL_ID -> authenticated.externalId
    session += SECURITY_TOKEN -> authenticated.securityToken
  }

  def getSession: Option[GameSession] = {
    for {
      intId <- session.get(INTERNAL_ID)
      extId <- session.get(EXTERNAL_ID)
      secT <- session.get(SECURITY_TOKEN)
    } yield GameSession(intId.asInstanceOf[ObjectId], extId.asInstanceOf[String], secT.asInstanceOf[String])
  }
}
