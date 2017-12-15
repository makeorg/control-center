package org.make.core.URI

import scala.scalajs.js.URIUtils

// Inspired by https://github.com/lemonlabsuk/scala-uri/blob/master/src/main/scala/com/netaporter/uri/dsl/UriDsl.scala
class UriDsl(val uri: String) extends AnyVal {

  private def urlify(unencodedUrl: String): String = URIUtils.encodeURIComponent(unencodedUrl)

  private def hasParams(uri: String) = uri.contains("?")

  def paramToString(param: (String, Any)): String = param match {
    case (key, Some(value)) => s"${urlify(key)}=${urlify(value.toString)}"
    case (key, value)       => s"${urlify(key)}=${urlify(value.toString)}"
  }

  def paramsToString(params: Seq[(String, Any)], separator: String = "&"): String =
    params.map(paramToString).mkString(separator)

  def ?(param: (String, Any)): String = param match {
    case (_, None) => s"$uri"
    case _         => s"$uri?${paramToString(param)}"
  }

  def &(param: (String, Any)): String = param match {
    case (_, None) => s"$uri"
    case _ =>
      if (hasParams(uri))
        s"$uri&${paramToString(param)}"
      else
        s"$uri?${paramToString(param)}"
  }

  def `#`(fragment: String) = s"$uri#$fragment"

  def /(path: String) = s"$uri/$path"

  def addParams(params: Seq[(String, Any)]): String =
    if (params.isEmpty) {
      uri
    } else if (hasParams(uri)) {
      s"$uri&${paramsToString(params)}"
    } else {
      s"$uri?${paramsToString(params)}"
    }

}
