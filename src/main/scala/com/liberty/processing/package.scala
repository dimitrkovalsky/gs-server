package com.liberty

import com.liberty.model.GameSession
import akka.actor.ActorRef
import com.liberty.requests.GenericRequest
import com.liberty.responses.GenericResponse
import com.liberty.errors.Error

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 15:01
 */
package object processing {

  case class ProcessingMessage(id: Long, senderRef: ActorRef, request: GenericRequest, session: GameSession)

  case class AuthMessage(senderRef: ActorRef, request: GenericRequest)

  case class RawMessage(senderRef: ActorRef, data: String, startProcessingTime: Long)

  case class ProcessingError(id: Long, error: Error)

  case class ProcessingCompleted(result: GenericResponse, startProcessingTime: Long)

  case class ProcessingAck(id: Long, result: GenericResponse)

  case class OnProcessing(id: Long, startTime: Long, processor: ActorRef)

  case class ProcessingTimeout(id: Long)


}
