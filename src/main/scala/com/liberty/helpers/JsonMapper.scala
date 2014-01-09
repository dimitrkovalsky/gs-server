package com.liberty.helpers

import com.fasterxml.jackson.databind.{SerializationFeature, ObjectMapper}
import com.liberty.requests.GenericRequest
import scala.reflect.runtime.universe._
import scala.collection.immutable.ListMap

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

  private def caseClassParamsOf[T: TypeTag]: ListMap[String, Type] = {
    val tpe = typeOf[T]
    val constructorSymbol = tpe.declaration(nme.CONSTRUCTOR)
    val defaultConstructor =
      if (constructorSymbol.isMethod) constructorSymbol.asMethod
      else {
        val ctors = constructorSymbol.asTerm.alternatives
        ctors.map {
          _.asMethod
        }.find {
          _.isPrimaryConstructor
        }.get
      }

    ListMap[String, Type]() ++ defaultConstructor.paramss.reduceLeft(_ ++ _).map {
      sym => sym.name.toString -> tpe.member(sym.name).asMethod.returnType
    }
  }

  def instantiate[T](clazz: java.lang.Class[T])(args: Array[AnyRef]): T = {
    val constructor = clazz.getConstructors()(0)

    constructor.newInstance(args:_*).asInstanceOf[T]
  }

  def convert[T: TypeTag](data: Any, clazz: java.lang.Class[T]) = {
    val map = data.asInstanceOf[java.util.LinkedHashMap[String, Object]]
    val params = caseClassParamsOf[T]
    var args = Array[AnyRef]()
    for (key <- params.keys) {
      args = args ++ Array(init(params(key).toString, map.get(key)))
    }
    instantiate[T](clazz)(args)
  }

  private def init(symbol: String, data: AnyRef): AnyRef = {
    symbol match {
      case "String" => data.asInstanceOf[String]
      case "Int" => data.asInstanceOf[Integer]
      case "Float" => data.asInstanceOf[java.lang.Float]
      case "Double" => data.asInstanceOf[java.lang.Double]
    }
  }


}
