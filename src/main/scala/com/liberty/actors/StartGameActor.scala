package com.liberty.actors

import com.liberty.processing.PermanentActor
import scala.concurrent.duration._
import com.liberty.messages.ResourceUpdateMessage
import akka.actor.Actor
import com.liberty.responses.{PermanentResponse, GenericResponse}

/**
 * User: dkovalskyi
 * Date: 10.01.14
 * Time: 16:00
 */
class StartGameActor extends PermanentActor {

  def handleInternalMessage: Actor.Receive = {
    case ResourceUpdateMessage => respondPermanent(PermanentResponse.resourceUpdateResponse(s"Updated resources for user ${session.externalId}"))
  }

  def handle(): Unit = {
    log.info(s"[StartGameActor] user : ${session.externalId} start game")

    context.system.scheduler.schedule(1.second, 1.second, self, ResourceUpdateMessage)
    context.become(handleInternalMessage)
    respondSuccess()
  }
}
