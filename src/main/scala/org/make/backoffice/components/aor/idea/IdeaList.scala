package org.make.backoffice.components.aor.idea

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Datagrid._
import org.make.backoffice.facades.AdminOnRest.EditButton._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Filter._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.List._
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.Idea
import org.make.client.Resource

object IdeaList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[ListProps, Unit](
        displayName = "IdeaList",
        render = self => {
          <.List(
            ^.perPage := 200,
            ^.title := "Ideas",
            ^.location := self.props.location,
            ^.resource := Resource.ideas,
            ^.hasCreate := true,
            ^.filters := ideaFilters(),
            ^.sort := Map("field" -> "name", "order" -> "DESC")
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.TextField(^.source := "name", ^.sortable := true)(),
              <.FunctionField(^.label := "theme", ^.render := { record =>
                val idea = record.asInstanceOf[Idea]
                idea.themeId.map { id =>
                  Configuration.getThemeFromThemeId(id)
                }
              })(),
              <.ReferenceField(
                ^.source := "operationId",
                ^.label := "operation",
                ^.reference := Resource.operations,
                ^.linkType := false,
                ^.allowEmpty := true,
                ^.sortable := false
              )(<.TextField(^.source := "slug")())
            )
          )
        }
      )

  def ideaFilters(): ReactElement = {
    <.Filter(^.resource := Resource.ideas)(
      Seq(
        <.TextInput(^.label := "Name", ^.source := "name", ^.alwaysOn := true)(),
        <.SelectInput(
          ^.label := "Theme",
          ^.source := "themeId",
          ^.alwaysOn := true,
          ^.allowEmpty := true,
          ^.choices := Configuration.choicesThemeFilter
        )(),
        <.ReferenceInput(
          ^.label := "Operation",
          ^.source := "operationId",
          ^.reference := Resource.operations,
          ^.alwaysOn := true,
          ^.allowEmpty := true
        )(<.SelectInput(^.optionText := "slug")()),
        <.SelectInput(
          ^.label := "Country",
          ^.source := "country",
          ^.alwaysOn := true,
          ^.allowEmpty := true,
          ^.choices := Configuration.choicesCountryFilter
        )()
      )
    )
  }

}
