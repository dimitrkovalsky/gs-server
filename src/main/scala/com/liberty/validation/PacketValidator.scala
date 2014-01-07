package com.liberty.validation

import com.liberty.processing.RawMessage
import com.liberty.requests.GenericRequest
import com.liberty.types.RequestType
import com.liberty.model.GameSession

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 15:39
 */
trait PacketValidator {
  def validateBase(message: RawMessage): Boolean = {
    message.data != null && message.data.length() > 0
  }

  // TODO : Security token validation
  def validateAuthentication(request: GenericRequest): Boolean = {
    require(request.requestType > 0)
    request.securityToken.isDefined || request.requestType == RequestType.RT_AUTHENTICATE
  }

  def validateSession(request: GenericRequest, session: Option[GameSession]): Boolean = {
    if (request.requestType == RequestType.RT_AUTHENTICATE)
      return true
    session.isDefined
  }
}
