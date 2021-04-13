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
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Menu._
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.MaterialUiIcons._

import scala.scalajs.js

object CustomMenu {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "CustomMenu",
        render = { self =>
          <.div()(
            <.DashboardMenuItem()(),
            <.Divider.empty,
            <.MenuItemLink(
              ^.to := s"/${Resource.proposals}",
              ^.primaryText := "Proposals to moderate",
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
              ^.to := s"/${Resource.ideaMappings}",
              ^.primaryText := "Idea mappings",
              ^.leftIcon := ideaMappingIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.MenuItemLink(
              ^.to := s"/${Resource.ideas}",
              ^.primaryText := "Ideas",
              ^.leftIcon := ideaIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.MenuItemLink(
              ^.to := s"/${Resource.topIdeas}",
              ^.primaryText := "Top Ideas",
              ^.leftIcon := topIdeasIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.Divider.empty,
            <.MenuItemLink(
              ^.to := s"/${Resource.moderators}",
              ^.primaryText := "Moderators",
              ^.leftIcon := moderatorIcon,
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
              ^.to := s"/${Resource.personalities}",
              ^.primaryText := "Personalities",
              ^.leftIcon := personalitiesIcon,
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
            <.MenuItemLink(
              ^.to := s"/${Resource.questionsConfiguration}",
              ^.primaryText := "Questions Configuration",
              ^.leftIcon := questionsConfigurationIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.Divider.empty,
            <.MenuItemLink(
              ^.to := s"/${Resource.features}",
              ^.primaryText := "Features",
              ^.leftIcon := featuresIcon,
              ^.onClick := self.props.native.onMenuTap.asInstanceOf[js.Function0[Unit]]
            )(),
            <.Divider.empty,
            self.props.native.logout
          )
        }
      )

}
