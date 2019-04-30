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

package org.make.backoffice.component.ideaMappings

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._

object EditIdeaMapping {

  case class EditIdeaMappingProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[EditIdeaMappingProps, Unit](
        displayName = "EditIdeaMapping",
        render = self => {
          <.div()(
            <.Edit(
              ^.resource := Resource.ideaMappings,
              ^.location := self.props.location,
              ^.`match` := self.props.`match`
            )(
              <.SimpleForm(^.redirect := false)(
                <.ReferenceField(
                  ^.source := "questionId",
                  ^.label := "question",
                  ^.reference := Resource.questions,
                  ^.linkType := false,
                  ^.sortable := false
                )(<.TextField(^.source := "slug")()),
                <.ReferenceField(
                  ^.source := "stakeTagId",
                  ^.label := "Stake Tag",
                  ^.reference := Resource.tags,
                  ^.linkType := false,
                  ^.sortable := false
                )(<.TextField(^.source := "label")()),
                <.ReferenceField(
                  ^.source := "solutionTypeTagId",
                  ^.label := "Solution Type Tag",
                  ^.reference := Resource.tags,
                  ^.linkType := false,
                  ^.sortable := false
                )(<.TextField(^.source := "label")()),
                <.ReferenceInput(
                  ^.source := "ideaId",
                  ^.label := "idea",
                  ^.translateLabel := ((label: String) => label),
                  ^.reference := Resource.ideas,
                  ^.linkType := false,
                  ^.sortable := false,
                  ^.sort := Map("field" -> "name", "order" -> "ASC"),
                )(<.SelectInput(^.source := "name")()),
                <.BooleanInput(^.source := "migrateProposals", ^.options := Map("defaultToggled" -> true))()
              )
            )
          )
        }
      )

}
