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

package org.make.backoffice.component.images

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.backoffice.facade.AdminOnRest.Fields.FieldsVirtualDOMAttributes
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.ReactDropzone.{DropzoneVirtualDOMAttributes, DropzoneVirtualDOMElements}
import org.make.backoffice.facade.reduxForm.Field._
import org.make.backoffice.facade.reduxForm.{FieldHolder, FieldInput}
import org.scalajs.dom.FormData
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object ImageUploadField {

  case class ImageUploadFieldProps(source: String,
                                   label: String,
                                   dropZoneClassName: String,
                                   previewClassName: String,
                                   uploadImage: FormData => Future[String],
                                   onError: Throwable    => Unit = _ => ())

  lazy val reactClass: ReactClass =
    React
      .createClass[ImageUploadFieldProps, Unit](
        displayName = "PictureUpload",
        render = { self =>
          <.Field(
            ^.name := self.props.wrapped.source,
            ^.source := self.props.wrapped.source,
            ^.label := self.props.wrapped.label,
            ^.component := { holder: FieldHolder =>
              <(innerReactClass)(
                ^.wrapped := ImageUploadInnerFieldProps(
                  holder.input,
                  self.props.wrapped.label,
                  self.props.wrapped.dropZoneClassName,
                  self.props.wrapped.previewClassName,
                  self.props.wrapped.uploadImage,
                  self.props.wrapped.onError
                )
              )()
            }
          )()
        }
      )

  case class ImageUploadInnerFieldProps(input: FieldInput,
                                        label: String,
                                        dropZoneClassName: String,
                                        previewClassName: String,
                                        uploadImage: FormData => Future[String],
                                        onError: Throwable    => Unit)

  case class ImageUploadFieldInnerState(value: String, previewUrl: String)

  lazy val innerReactClass: ReactClass =
    React
      .createClass[ImageUploadInnerFieldProps, ImageUploadFieldInnerState](
        displayName = "PictureUpload",
        getInitialState = { self =>
          val url = self.props.wrapped.input.value
          ImageUploadFieldInnerState(value = url, previewUrl = url)
        },
        componentWillReceiveProps = { (self, newProps) =>
          val url = newProps.wrapped.input.value
          self.setState(_.copy(value = url, previewUrl = url))
        },
        render = { self =>
          val updateState = { event: FormSyntheticEvent[HTMLInputElement] =>
            val value = event.target.value
            self.setState(_.copy(value = value))
          }

          val updateRecord = { () =>
            val value = self.state.value
            if (value == "") {
              self.props.wrapped.input.onChange(null)
            } else {
              self.props.wrapped.input.onChange(value)
            }
          }

          def uploadImage: js.Array[ImageFile] => Unit = { files =>
            val file = files.head
            self.setState(_.copy(previewUrl = file.preview))
            val formData = new FormData()
            formData.append("data", file)
            self.props.wrapped.uploadImage(formData).onComplete {
              case Success(url) =>
                self.setState(_.copy(value = url, previewUrl = url))
                self.props.wrapped.input.onChange(url)
              case Failure(e) => self.props.wrapped.onError(e)
            }
          }

          <.div()(
            <.Dropzone(
              ^.multiple := false,
              ^.className := self.props.wrapped.dropZoneClassName,
              ^.onDropDropzone := uploadImage
            )("Picture"),
            <.img(^.src := self.state.value, ^.className := self.props.wrapped.previewClassName)(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := self.props.wrapped.label,
              ^.value := self.state.value,
              ^.onChange := updateState,
              ^.onBlur := updateRecord,
              ^.fullWidth := true
            )()
          )
        }
      )

}

@js.native
trait ImageFile extends js.Object {
  val preview: String
}
