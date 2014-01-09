package com.liberty

import com.liberty.requests.{GenericRequest, AuthRequest}
import scala.reflect.runtime.universe._
import scala.collection.immutable.ListMap
import com.liberty.helpers.JsonMapper
import com.liberty.annotation.{Handler, RequestData,}
import com.liberty.annotation.Data

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 12:29
 */
object Runner extends App {

  val req = "{\"requestType\":100,\"requestData\":{\"googleId\":\"100500\"},\"securityToken\":null}"

  val gr: GenericRequest = JsonMapper.parseRequest(req)

  //println(JsonMapper.convert[AuthRequest](gr.requestData, classOf[AuthRequest]))

  val data = new Data()
  data.processParsing(gr.requestData)
  data.print()
}

class Data {
  @RequestData(classOf[AuthRequest])
  var auth: AuthRequest = _

  def print() {
    println(auth)
  }

  def processParsing(data: Any) {
    val clazz = getClass

    for (field <- clazz.getDeclaredFields) {
      if (field.isAnnotationPresent(Data)) {
        field.setAccessible(true)
        println(field.getAnnotation(classOf[RequestData]).value)
        val res = JsonMapper.convert(data, field.getAnnotation(classOf[RequestData]).value)
        field.set(this, res)
      }
    }
  }
}



