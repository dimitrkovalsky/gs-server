package com.liberty.requests

import com.fasterxml.jackson.annotation.JsonAnyGetter
import scala.beans.BeanProperty

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 21:33
 */
case class GenericRequest(@BeanProperty requestType: Int, @BeanProperty securityToken: Option[String],
                          @BeanProperty requestData: Any) {
  def this() = this(0, None, null)
}
