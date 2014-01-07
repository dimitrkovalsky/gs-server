package com.liberty.responses

import com.liberty.types.{RequestType, Responses}
import com.liberty.errors.Error

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 15:31
 */
/**
 * @param responseStatus
 * @param requestType
 * @param responseData
 * @param pst - Processing Time in millis
 */
case class GenericResponse(responseStatus: Int, requestType: Option[Int], responseData: Option[Any], pst: Long = 0) {}

object GenericResponse {
  def apply(requestType: Int, any: Any): GenericResponse =
    GenericResponse(Responses.RESPONSE_STATUS_OK, Some(requestType), Some(any))

  def authenticatedResponse(auth: Authenticated) = apply(RequestType.RT_AUTHENTICATE, auth)

  def errorResponse(error: Error) = GenericResponse(Responses.RESPONSE_STATUS_FAIL, None, Some(error))
}