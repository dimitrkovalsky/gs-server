package com.liberty.validation

import com.liberty.requests.GenericRequest

/**
 * User: Dimitr
 * Date: 07.01.14
 * Time: 16:01
 */
trait Validatable {
  def validate(request: GenericRequest): Unit
}
