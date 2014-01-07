package com.liberty.processing

import akka.actor.Actor
import com.liberty.validation.Validatable
import com.liberty.requests.{GenericRequest, AuthRequest}
import com.codahale.jerkson.Json
import com.liberty.errors.Error
import com.liberty.responses.Authenticated
import java.util.UUID
import org.bson.types.ObjectId
import com.liberty.helpers.JsonMapper

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 11:45
 */
class AuthenticationActor extends CommandActor with Validatable {
  var auth: AuthRequest = _

  def handleSuccess(): Unit = {}


  def handle(message: ProcessingMessage): Unit = {
    if (!validateGoogleId(auth.googleId)) {
      handleFail(message, Error.validationError("Google id is invalid : " + auth.googleId))
      return
    }

    val sk = UUID.randomUUID().toString
    val authResult = Authenticated(sk, new ObjectId(), auth.googleId)
    sender ! authResult
  }

  def validateGoogleId(googleId: String): Boolean = {
    googleId.length > 0
  }


  override def validate(request: GenericRequest): Unit = {
    auth = JsonMapper.convertValue(request.requestData, classOf[AuthRequest])
  }
}
