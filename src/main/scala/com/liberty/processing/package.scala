package com.liberty

import com.liberty.model.GameSession
import akka.actor.ActorRef
import com.liberty.requests.GenericRequest
import com.liberty.responses.{PermanentResponse, Authenticated, GenericResponse}
import com.liberty.errors.Error

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 15:01
 */
package object processing {
  sealed trait ProcessingResponse

  case class ProcessingMessage(id: Long, frontRef: ActorRef, request: GenericRequest, session: GameSession)

  case class AuthMessage(senderRef: ActorRef, request: GenericRequest)

  case class RawMessage(senderRef: ActorRef, data: String, startProcessingTime: Long)

  case class ProcessingError(id: Long, error: Error) extends ProcessingResponse

  case class ProcessingCompleted(result: GenericResponse, startProcessingTime: Long) extends ProcessingResponse

  case class ProcessingAck(id: Long, result: GenericResponse) extends ProcessingResponse

  case class AuthenticatedAck(id: Long, auth: Authenticated) extends ProcessingResponse

  case class PermanentActorAck(id: Long, result: GenericResponse) extends ProcessingResponse

  case class PermanentMessage(result:PermanentResponse) extends ProcessingResponse

  case class OnProcessing(id: Long, startTime: Long, processor: ActorRef)

  case class ProcessingTimeout(id: Long)


}
