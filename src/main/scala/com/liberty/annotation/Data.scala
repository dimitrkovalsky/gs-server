package com.liberty.annotation

import scala.annotation.StaticAnnotation

/**
 * User: dkovalskyi
 * Date: 09.01.14
 * Time: 16:52
 */
class Data(val clazz: Class[_]) extends StaticAnnotation {}
