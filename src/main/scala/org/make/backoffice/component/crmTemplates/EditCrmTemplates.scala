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
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._

object EditCrmTemplates {

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[RouterProps, Unit](
        displayName = "EditCrmTemplates",
        render = { self =>
          <.Edit(
            ^.resource := Resource.crmTemplates,
            ^.location := self.props.location,
            ^.`match` := self.props.`match`,
            ^.hasList := true
          )(
            <.SimpleForm()(
              <.h3()("B2C Templates :"),
              <.NumberInput(^.source := "registration", ^.options := Map("fullWidth" -> true))(),
              <.NumberInput(
                ^.source := "resendRegistration",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Resend Validation Email"
              )(),
              <.NumberInput(^.source := "welcome", ^.options := Map("fullWidth" -> true))(),
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
