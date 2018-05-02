package org.make.backoffice.service.tag

import org.make.backoffice.model.Tag
import org.make.backoffice.client.ListDataResponse
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._
import org.make.backoffice.service.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object TagService extends ApiService with CirceClassFormatters {
  override val resourceName: String = "tags"

  def getTagById(id: String): Future[Tag] =
    client.get[Tag](resourceName / id).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to Tag: failed cursor $e")
        throw e
    }

  def getTagsByIds(ids: js.Array[String]): Future[ListDataResponse[Tag]] =
    tags.map { tags =>
      tags.filter(tag => ids.contains(tag.id))
    }.map(ListDataResponse.apply)

  def tags: Future[Seq[Tag]] =
    client.get[Seq[Tag]](resourceName).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to Tag: failed cursor $e")
        throw e
    }
}
