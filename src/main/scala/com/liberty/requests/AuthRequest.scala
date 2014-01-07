package com.liberty.requests

import scala.beans.BeanProperty

/**
 * User: Dimitr
 * Date: 07.01.14
 * Time: 16:09
 */
case class AuthRequest(@BeanProperty googleId: String) {
  def this() = this("")
}


