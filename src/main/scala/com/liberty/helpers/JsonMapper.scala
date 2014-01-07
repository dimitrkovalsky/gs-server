package com.liberty.helpers

import com.fasterxml.jackson.databind.{SerializationFeature, SerializationConfig, ObjectMapper}
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.liberty.requests.GenericRequest
import com.liberty.requests.GenericRequest

/**
 * User: Dimitr
 * Date: 07.01.14
 * Time: 21:33
 */
case object JsonMapper extends ObjectMapper {
  configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

  def parseRequest(request: String): GenericRequest = {
    this.readValue[GenericRequest](request, classOf[GenericRequest])
  }

}
