package org.make.core

package object URI {
  import scala.language.implicitConversions

  implicit def stringToUriDsl(s: String): UriDsl = new UriDsl(s)
}
