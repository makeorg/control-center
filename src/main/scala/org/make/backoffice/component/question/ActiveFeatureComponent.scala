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

package org.make.backoffice.component.question

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.ActiveFeature
import org.make.backoffice.service.feature.ActiveFeatureService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object ActiveFeatureComponent {

  case class ActiveFeatureProps(activeFeaturesList: Seq[ActiveFeature], questionId: String, reloadComponent: () => Unit)
  case class ActiveFeatureState(checked: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[ActiveFeatureProps, ActiveFeatureState](
        displayName = "ActiveFeature",
        getInitialState = { self =>
          val featureId = self.props.native.record.id.toString
          val checked = self.props.wrapped.activeFeaturesList
            .exists(af => af.maybeQuestionId.contains(self.props.wrapped.questionId) && af.featureId == featureId)
          ActiveFeatureState(checked = checked)
        },
        componentWillReceiveProps = { (self, props) =>
          val featureId = props.native.record.id.toString
          val checked = props.wrapped.activeFeaturesList
            .exists(af => af.maybeQuestionId.contains(props.wrapped.questionId) && af.featureId == featureId)
          self.setState(_.copy(checked = checked))
        },
        render = { self =>
          val featureId = self.props.native.record.id.toString
          val activeFeature: Option[ActiveFeature] = self.props.wrapped.activeFeaturesList
            .find(af => af.maybeQuestionId.contains(self.props.wrapped.questionId) && af.featureId == featureId)

          def handleActiveFeature: (js.Object, Boolean) => Unit = {
            (_, isChecked) =>
              if (isChecked) {
                ActiveFeatureService.createActiveFeature(featureId, self.props.wrapped.questionId).onComplete {
                  case Success(_) =>
                    self.setState(_.copy(checked = true))
                    self.props.wrapped.reloadComponent()
                  case Failure(_) =>
                }
              } else {
                ActiveFeatureService
                  .deleteActiveFeature(activeFeature.map(_.activeFeatureId).getOrElse(""))
                  .onComplete {
                    case Success(_) =>
                      self.setState(_.copy(checked = false))
                      self.props.wrapped.reloadComponent()
                    case Failure(_) =>
                  }
              }
          }

          <.Checkbox(^.checked := self.state.checked, ^.onCheck := handleActiveFeature)()
        }
      )

}
