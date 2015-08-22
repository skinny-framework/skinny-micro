package org.scalatra.util

package object conversion {

  @deprecated("Use skinny.micro.implicits.TypeConverter instead", since = "2.0.0")
  type TypeConverter[S, T] = skinny.micro.implicits.TypeConverter[S, T]

}
