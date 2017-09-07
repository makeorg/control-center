package org.make.core

import scala.language.implicitConversions

package object URI {

  implicit def stringToUriDsl(s: String): UriDsl = new UriDsl(s)
}
