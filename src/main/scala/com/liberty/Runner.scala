package com.liberty

import com.liberty.helpers.JsonMapper
import com.liberty.model.GameSession
import org.bson.types.ObjectId
import com.codahale.jerkson.Json
import com.liberty.requests.{GenericRequest, AuthRequest}
import com.fasterxml.jackson.databind.{ObjectMapper, JsonNode}
import org.json4s.ParserUtil

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 12:29
 */
object Runner extends App {
  val req = "{\"requestType\":100,\"requestData\":{\"googleId\":\"100500\"},\"securityToken\":null}"

  val gr: GenericRequest = JsonMapper.parseRequest(req)

  println(JsonMapper.convertValue(gr.requestData, classOf[AuthRequest]))
}
