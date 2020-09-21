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

package org.make.backoffice.component.crmTemplates

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Create._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.facade.Choice
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.service.question.QuestionService
import org.make.backoffice.util.Configuration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object CreateCrmTemplates {

  def apply(): ReactClass = reactClass

  private final case class State(countries: Seq[Choice])

  private lazy val reactClass: ReactClass =
    React
      .createClass[RouterProps, State](
        displayName = "CreateCrmTemplates",
        getInitialState = _ => State(Configuration.choicesCountry),
        render = { self =>
          val onQuestionChange: OnChangeReference = {
            (_, value) =>
              value
                .fold(Future.successful(Configuration.choicesCountry))(
                  id => QuestionService.getQuestionById(id).map(r => Configuration.choicesCountry(r.data.countries))
                )
                .onComplete {
                  case Success(countries) => self.setState(_.copy(countries = countries))
                  case Failure(e) =>
                    js.Dynamic.global.console.error(s"Could not fetch question $value : $e")
                    self.setState(_.copy(countries = Nil))
                }
          }

          <.Create(^.resource := Resource.crmTemplates, ^.location := self.props.location, ^.hasList := true)(
            <.SimpleForm()(
              <.ReferenceInput(
                ^.source := "questionId",
                ^.label := "question",
                ^.translateLabel := ((label: String) => label),
                ^.reference := Resource.questions,
                ^.allowEmpty := true,
                ^.sort := Map("field" -> "slug", "order" -> "ASC"),
                ^.perPage := 200,
                ^.onChangeReference := onQuestionChange
              )(<.SelectInput(^.optionText := "slug", ^.options := Map("fullWidth" -> true))()),
              <.SelectInput(
                ^.source := "country",
                ^.choices := self.state.countries,
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              Configuration.choiceLanguage.map {
                case (country, languages) =>
                  <.DependentInput(^.dependsOn := "country", ^.dependsValue := country)(
                    <.SelectInput(
                      ^.source := "language",
                      ^.choices := languages,
                      ^.allowEmpty := true,
                      ^.options := Map("fullWidth" -> true)
                    )()
                  )
              },
              <.h3()("B2C Templates :"),
              <.NumberInput(^.source := "registration", ^.options := Map("fullWidth" -> true))(),
              <.NumberInput(
                ^.source := "resendRegistration",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Resend Validation Email"
              )(),
              <.NumberInput(^.source := "welcome", ^.options := Map("fullWidth" -> true), ^.label := "Welcome")(),
              <.NumberInput(
                ^.source := "proposalAccepted",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Proposal Accepted"
              )(),
              <.NumberInput(
                ^.source := "proposalRefused",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Proposal Refused"
              )(),
              <.NumberInput(
                ^.source := "forgottenPassword",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Forgotten Password"
              )(),
              <.h3()("B2B Templates :"),
              <.NumberInput(
                ^.source := "proposalAcceptedOrganisation",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Proposal Accepted"
              )(),
              <.NumberInput(
                ^.source := "proposalRefusedOrganisation",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Proposal Refused"
              )(),
              <.NumberInput(
                ^.source := "forgottenPasswordOrganisation",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Forgotten Password"
              )(),
              <.NumberInput(
                ^.source := "organisationEmailChangeConfirmation",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Email Change Confirmation"
              )(),
              <.NumberInput(
                ^.source := "registrationB2B",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Registration"
              )()
            )
          )
        }
      )

}
