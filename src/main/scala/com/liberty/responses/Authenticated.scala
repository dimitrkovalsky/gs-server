package com.liberty.responses

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonIgnore}
import org.bson.types.ObjectId

/**
 * User: Dimitr
 * Date: 05.01.14
 * Time: 20:31
 */

@JsonIgnoreProperties(Array("internalId", "externalId"))
case class Authenticated(securityToken: String,
                         internalId: ObjectId,
                         externalId: String) {}
