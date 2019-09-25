/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package org.make.backoffice.service.homepage

import io.circe.Decoder
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.uri._
import org.scalajs.dom.FormData
import org.scalajs.dom.ext.Ajax.InputData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object HomepageService extends ApiService {

  override val resourceName = "admin/views/home"

  def uploadImage(formData: FormData): Future[UploadResponse] = {
    val data: InputData = InputData.formdata2ajax(formData)
    client.post[UploadResponse](resourceName / "images", data = data, includeContentType = false).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to UploadResponse: failed cursor $e")
        throw e
    }
  }

}

@js.native
trait UploadResponse extends js.Object {
  val path: String
}

object UploadResponse {
  def apply(path: String): UploadResponse = js.Dynamic.literal(path = path).asInstanceOf[UploadResponse]

  implicit lazy val decoder: Decoder[UploadResponse] = Decoder.forProduct1("path")(UploadResponse.apply)
}
