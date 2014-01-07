package com.liberty.processing

import xitrum.Log
import scala.Exception
import com.liberty.types.RequestType
import com.liberty.annotation.Handler
import java.util

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 14:05
 */
object CommandRegistry extends Log {
  private val commands = init()

  private def init(): util.HashMap[Integer, Class[_ <: CommandActor]] = {
    val clazz = classOf[RequestType]
    val map = new util.HashMap[Integer, Class[_ <: CommandActor]]()
    try {
      for (field <- clazz.getDeclaredFields) {
        if (field.isAnnotationPresent(classOf[Handler])) {
          field.setAccessible(true)
          map.put(field.getInt(clazz), field.getAnnotation(classOf[Handler]).value)
        }
      }
      log.info(s"[CommandRegistry] loaded ${map.size()} commands")
    }
    catch {
      case e: Exception => log.error("[CommandRegistry]  initialization error " + e.getMessage)
    }
    map
  }

  def getCommandClass(requestType: Int): Option[Class[_ <: CommandActor]] = {
    val handler = commands.get(requestType)
    if (handler != null)
      Some(handler)
    else None
  }
}
