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

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Menu._
import org.make.backoffice.facade.MaterialUi._

import scala.scalajs.js

object CustomMenu {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "CustomMenu",
        render = { self =>
          val proposalSvgPath =
            "M20 2H4c-1.1 0-1.99.9-1.99 2L2 22l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 9h12v2H6V9zm8 5H6v-2h8v2zm4-6H6V6h12v2z"
          val tagSvgPath =
            "M17.63 5.84C17.27 5.33 16.67 5 16 5L5 5.01C3.9 5.01 3 5.9 3 7v10c0 1.1.9 1.99 2 1.99L16 19c.67 0 1.27-.33 1.63-.84L22 12l-4.37-6.16z"
          val ideaSvgPath =
            "M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96z"
          val ideaMappingSvgPath =
            "M4 14h4v-4H4v4zm0 5h4v-4H4v4zM4 9h4V5H4v4zm5 5h12v-4H9v4zm0 5h12v-4H9v4zM9 5v4h12V5H9z"
          val operationSvgPath =
            "M15 9H9v6h6V9zm-2 4h-2v-2h2v2zm8-2V9h-2V7c0-1.1-.9-2-2-2h-2V3h-2v2h-2V3H9v2H7c-1.1 0-2 .9-2 2v2H3v2h2v2H3v2h2v2c0 1.1.9 2 2 2h2v2h2v-2h2v2h2v-2h2c1.1 0 2-.9 2-2v-2h2v-2h-2v-2h2zm-4 6H7V7h10v10z"
          val questionSvgPath =
            "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 17h-2v-2h2v2zm2.07-7.75l-.9.92C13.45 12.9 13 13.5 13 15h-2v-.5c0-1.1.45-2.1 1.17-2.83l1.24-1.26c.37-.36.59-.86.59-1.41 0-1.1-.9-2-2-2s-2 .9-2 2H8c0-2.21 1.79-4 4-4s4 1.79 4 4c0 .88-.36 1.68-.93 2.25z"
          val organisationSvgPath =
            "M12 7V3H2v18h20V7H12zM6 19H4v-2h2v2zm0-4H4v-2h2v2zm0-4H4V9h2v2zm0-4H4V5h2v2zm4 12H8v-2h2v2zm0-4H8v-2h2v2zm0-4H8V9h2v2zm0-4H8V5h2v2zm10 12h-8v-2h2v-2h-2v-2h2v-2h-2V9h8v10zm-2-8h-2v2h2v-2zm0 4h-2v2h2v-2z"
          val moderatorSvgPath =
            "M3 5v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2H5c-1.11 0-2 .9-2 2zm12 4c0 1.66-1.34 3-3 3s-3-1.34-3-3 1.34-3 3-3 3 1.34 3 3zm-9 8c0-2 4-3.1 6-3.1s6 1.1 6 3.1v1H6v-1z"

          val proposalIcon: ReactElement =
            <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> proposalSvgPath)))

          val tagIcon: ReactElement = <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> tagSvgPath)))

          val ideaIcon: ReactElement = <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> ideaSvgPath)))

          val ideaMappingIcon: ReactElement =
            <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> ideaMappingSvgPath)))

          val operationIcon: ReactElement =
            <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> operationSvgPath)))

          val questionIcon: ReactElement =
            <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> questionSvgPath)))

          val organisationIcon: ReactElement =
            <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> organisationSvgPath)))

          val moderatorIcon: ReactElement =
            <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> moderatorSvgPath)))

          <.div()(
            <.DashboardMenuItem()(),
            <.Divider.empty,
            <.MenuItemLink(
              ^.to := s"/${Resource.proposals}",
              ^.primaryText := "Proposals",
              ^.leftIcon := proposalIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.MenuItemLink(
              ^.to := s"/${Resource.toEnrichProposals}",
              ^.primaryText := "To enrich proposals",
              ^.leftIcon := proposalIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.MenuItemLink(
              ^.to := s"/${Resource.validatedProposals}",
              ^.primaryText := "Validated proposals",
              ^.leftIcon := proposalIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.Divider.empty,
            <.MenuItemLink(
              ^.to := s"/${Resource.tags}",
              ^.primaryText := "Tags",
              ^.leftIcon := tagIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.MenuItemLink(
              ^.to := s"/${Resource.ideas}",
              ^.primaryText := "Ideas",
              ^.leftIcon := ideaIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.MenuItemLink(
              ^.to := s"/${Resource.ideaMappings}",
              ^.primaryText := "Idea mappings",
              ^.leftIcon := ideaMappingIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.Divider.empty,
            <.MenuItemLink(
              ^.to := s"/${Resource.operations}",
              ^.primaryText := "Operations",
              ^.leftIcon := operationIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.MenuItemLink(
              ^.to := s"/${Resource.operationsOfQuestions}",
              ^.primaryText := "Questions",
              ^.leftIcon := questionIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.Divider.empty,
            <.MenuItemLink(
              ^.to := s"/${Resource.organisations}",
              ^.primaryText := "Organisations",
              ^.leftIcon := organisationIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.MenuItemLink(
              ^.to := s"/${Resource.moderators}",
              ^.primaryText := "Moderators",
              ^.leftIcon := moderatorIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.Divider.empty,
            self.props.native.logout
          )
        }
      )

}
