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

package org.make.backoffice.component

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.reduxForm.FieldInput
import org.scalajs.dom.raw.HTMLInputElement

object CustomAORValueInput {
  final case class CustomAORValueProps(initialValue: String, input: FieldInput, label: String)
  final case class CustomAORValueState(value: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[CustomAORValueProps, CustomAORValueState](
        displayName = "CustomAORValueInput",
        componentDidMount = { self =>
          if (self.props.wrapped.input.value != self.props.wrapped.initialValue) {
            if (self.props.wrapped.initialValue == "") {
              self.props.wrapped.input.onChange(null)
            } else {
              self.props.wrapped.input.onChange(self.props.wrapped.initialValue)
            }
          }
        },
        getInitialState = { self =>
          CustomAORValueState(self.props.wrapped.initialValue)
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

          <.TextFieldMaterialUi(
            ^.floatingLabelText := self.props.wrapped.label,
            ^.value := self.state.value,
            ^.fullWidth := true,
            ^.onChange := updateState,
            ^.onBlur := updateRecord,
          )()
        }
      )

}
