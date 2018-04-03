package org.make.backoffice.component.idea

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.util.Configuration
import org.make.backoffice.model.Idea
import org.make.backoffice.client.Resource

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
