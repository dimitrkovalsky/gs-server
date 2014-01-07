package com.liberty.controllers

import xitrum._
import xitrum.WebSocketText
import xitrum.annotation.WEBSOCKET
import com.liberty.errors.Error
import akka.actor.{Props, ActorRef}
import com.liberty.responses.GenericResponse
import com.codahale.jerkson.Json

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 12:55
 */
@WEBSOCKET("backend")
class FrontController extends WebSocketActor {

  import com.liberty.processing._

  val interpreter: ActorRef = context.system.actorOf(Props[Interpreter](new Interpreter(self, session)))

  override def execute(): Unit = {
    try {
      context.become {
        case WebSocketText(data) =>
          log.info("REQUEST <<<<<< " + data)
          interpreter ! RawMessage(self, data, System.currentTimeMillis())

        case ProcessingCompleted(resp, time) => respond(resp.copy(pst = System.currentTimeMillis() - time))

        case msg: Any => log.warn("[FrontController] unknown message : " + msg)
      }
    }
    catch {
      case ex: Throwable =>
        log.error("[FrontController] Error occurred ", ex)
        respondError(Error.processingFailed)
    }
  }

  override def postRestart(reason: Throwable) {
    super.postRestart(reason)
    log.warn(s"Restarted because of ${reason.getMessage}")
  }

  override def postStop() {
    log.debug("[FrontController] onClose")
    context.stop(interpreter)
    super.postStop()
  }

  def respondError(err: Error) {
    respond(GenericResponse.errorResponse(err))
  }

  def respond(response: GenericResponse) {
    log.info("RESPONSE >>>>>>" + Json.generate(response))
    respondWebSocketText(Json.generate(response))
  }

}
