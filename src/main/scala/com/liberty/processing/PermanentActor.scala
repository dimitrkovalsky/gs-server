package com.liberty.processing

import com.liberty.responses.{PermanentResponse, GenericResponse}
import akka.actor.ActorRef

/**
 * User: dkovalskyi
 * Date: 10.01.14
 * Time: 15:46
 */
trait PermanentActor extends CommandActor {
  implicit val execution = context.system.dispatcher
  var frontController: ActorRef = _

  override def setData(message: ProcessingMessage) {
    super.setData(message)
    frontController = message.frontRef
  }

  override def respondSuccess(response: GenericResponse) {
    requester ! PermanentActorAck(requestId, response)
  }

  def respondPermanent(response: PermanentResponse) {
    frontController ! PermanentMessage(response)
  }
}
