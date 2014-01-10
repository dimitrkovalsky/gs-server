package com.liberty.actors

import com.liberty.validation.Validatable
import com.liberty.requests.{GenericRequest, AuthRequest}
import com.liberty.errors.Error
import com.liberty.responses.Authenticated
import java.util.UUID
import org.bson.types.ObjectId
import com.liberty.helpers.JsonMapper
import com.liberty.annotation.RequestData
import com.liberty.processing.{AuthenticatedAck, CommandActor}

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 11:45
 */
class AuthenticationActor extends CommandActor with Validatable {

  var auth: AuthRequest = _

  def handle(): Unit = {
    if (!validateGoogleId(auth.googleId)) {
      respondFail(Error.validationError("Google id is invalid : " + auth.googleId))
      return
    }

    val sk = UUID.randomUUID().toString
    val authResult = Authenticated(sk, new ObjectId(), auth.googleId)

    requester ! AuthenticatedAck(requestId, authResult)
  }

  def validateGoogleId(googleId: String): Boolean = {
    googleId.length > 0
  }


  override def validate(request: GenericRequest): Unit = {
    auth = JsonMapper.convert(request.requestData, classOf[AuthRequest])
  }
}
