package org.make.services.tag

import org.make.backoffice.models.Tag
import org.make.core.CirceClassFormatters
import org.make.core.URI._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

trait TagServiceComponent {
  def tagService: TagService = new TagService

  class TagService extends ApiService with CirceClassFormatters {
    override val resourceName: String = "tags"

    def getTagById(id: String): Future[Tag] =
      client.get[Tag](resourceName / id).recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to Tag: failed cursor $e")
          throw e
      }

    def tags: Future[Seq[Tag]] =
      client.get[Seq[Tag]](resourceName).recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to Tag: failed cursor $e")
          throw e
      }
  }
}

object TagServiceComponent extends TagServiceComponent
