package com.liberty.types

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 14:03
 */

object Responses {
  final val GENERIC_RESPONSE = 100
  final val PERMANENT_RESPONSE = 101

  final val RESPONSE_STATUS_OK: Int = 200
  final val RESPONSE_STATUS_FAIL: Int = 201

  object PermanentResponse {
    val PR_RESOURCE_UPDATE = 400
  }

}
