package org.make.backoffice.service.tag

import org.make.backoffice.model.TagTypeResponse.toTagType
import org.make.backoffice.model.{TagType, TagTypeResponse}
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object TagTypeService extends ApiService with CirceClassFormatters {
  override val resourceName: String = "moderation/tag-types"

  def tagTypes: Future[Seq[TagType]] =
    client
      .get[Seq[TagTypeResponse]](s"$resourceName")
      .map(tagTypes => tagTypes.map(toTagType))
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to TagType: failed cursor $e")
          throw e
      }
}
