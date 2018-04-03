package org.make.backoffice.util

import scala.language.implicitConversions

package object uri {

  implicit def stringToUriDsl(s: String): UriDsl = new UriDsl(s)
}
