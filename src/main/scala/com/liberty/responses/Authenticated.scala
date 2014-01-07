package com.liberty.responses

import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 20:31
 */

case class Authenticated(securityToken: String,
                          @JsonIgnore internalId: ObjectId,
                          @JsonIgnore externalId: String) {}
