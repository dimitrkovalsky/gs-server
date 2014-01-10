package com.liberty.responses

import com.liberty.types.{RequestType, Responses}
import com.liberty.errors.Error
import com.liberty.requests.GenericRequest
import com.liberty.types.Responses._

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 15:31
 */
class ServerResponse(responseStatus: Int, requestType: Option[Int], responseType: Option[Int], responseData: Option[Any], pst: Long) {}

/**
 * @param responseStatus
 * @param requestType
 * @param responseData
 * @param pst - Processing Time in millis
 */
case class GenericResponse(responseStatus: Int, requestType: Option[Int], responseType: Option[Int], responseData: Option[Any], pst: Long = 0)
  extends ServerResponse(responseStatus, requestType, responseType, responseData, pst = 0) {}

case class PermanentResponse(responseStatus: Int, responseType: Option[Int], responseData: Option[Any])
  extends ServerResponse(responseStatus: Int, None, responseType, responseData, 0)

object GenericResponse {
  def apply(requestType: Int, any: Any): GenericResponse =
    GenericResponse(RESPONSE_STATUS_OK, Some(requestType), Some(requestType), Some(any))

  def apply(request: GenericRequest): GenericResponse =
    GenericResponse(RESPONSE_STATUS_OK, Some(request.requestType), Some(request.requestType), None)

  def authenticatedResponse(auth: Authenticated) = apply(RequestType.RT_AUTHENTICATE, auth)

  def errorResponse(error: Error) = GenericResponse(RESPONSE_STATUS_FAIL, None, None, Some(error))
}

object PermanentResponse {
  def apply(responseType: Int, any: Any): PermanentResponse =
    PermanentResponse(RESPONSE_STATUS_OK, Some(responseType), Some(any))

  def resourceUpdateResponse(responseData: Any) = PermanentResponse(Responses.PermanentResponse.PR_RESOURCE_UPDATE, responseData)

}