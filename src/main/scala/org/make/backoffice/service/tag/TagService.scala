package org.make.backoffice.service.tag

import org.make.backoffice.model.{Tag, TagResponse}
import org.make.backoffice.model.TagResponse._
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object TagService extends ApiService with CirceClassFormatters {
  override val resourceName: String = "moderation/tags"

  def tags: Future[Seq[Tag]] =
    client.get[Seq[TagResponse]](resourceName).map(tags => tags.map(toTag)).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to Tag: failed cursor $e")
        throw e
    }
}
