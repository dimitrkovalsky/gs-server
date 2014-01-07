package com.liberty.model

import org.bson.types.ObjectId

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 11:51
 */
case class GameSession(internalId: ObjectId, externalId: String, securityToken: String) {}
