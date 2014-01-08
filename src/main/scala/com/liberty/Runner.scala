package com.liberty

import com.liberty.requests.{GenericRequest, AuthRequest}
import scala.reflect.runtime.universe._
import scala.collection.immutable.ListMap
import com.liberty.helpers.JsonMapper

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 12:29
 */
object Runner extends App {

  def caseClassParamsOf[T: TypeTag]: ListMap[String, Type] = {
    val tpe = typeOf[T]
    val constructorSymbol = tpe.declaration(nme.CONSTRUCTOR)
    val defaultConstructor =
      if (constructorSymbol.isMethod) constructorSymbol.asMethod
      else {
        val ctors = constructorSymbol.asTerm.alternatives
        ctors.map { _.asMethod }.find { _.isPrimaryConstructor }.get
      }

    ListMap[String, Type]() ++ defaultConstructor.paramss.reduceLeft(_ ++ _).map {
      sym => sym.name.toString -> tpe.member(sym.name).asMethod.returnType
    }
  }

  def instantiate(clazz: java.lang.Class[_])(args:AnyRef*): AnyRef = {
    val constructor = clazz.getConstructors()(0)
    constructor.newInstance(args:_*).asInstanceOf[AnyRef]
  }

 // println(instantiate(classOf[AuthRequest])("someId"))
 val req = "{\"requestType\":100,\"requestData\":{\"googleId\":\"100500\", \"id\":100, \"fl\":100.125},\"securityToken\":null}"

  val gr: GenericRequest = JsonMapper.parseRequest(req)
//  println(gr.requestData)
  println(JsonMapper.convert[AuthRequest](gr.requestData, classOf[AuthRequest]))
//  val ars = Array[AnyRef]("Foobar")
//  val constructor = classOf[AuthRequest].getConstructors()(0)
//  println(constructor)
//  println(caseClassParamsOf[AuthRequest])
}
