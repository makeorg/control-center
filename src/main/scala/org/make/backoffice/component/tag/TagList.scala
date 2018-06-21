package org.make.backoffice.component.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.model.Tag
import org.make.backoffice.util.Configuration

object TagList {

  case class TagListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[TagListProps, Unit](
        displayName = "TagList",
        render = self => {
          <.List(
            ^.perPage := 50,
            ^.title := "Tags",
            ^.sort := Map("field" -> "label", "order" -> "ASC"),
            ^.location := self.props.location,
            ^.resource := Resource.tags,
            ^.hasCreate := true,
            ^.filters := tagFilters()
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.TextField(^.source := "label")(),
              <.FunctionField(^.label := "theme", ^.render := { record =>
                val tag = record.asInstanceOf[Tag]
                tag.themeId.map { id =>
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
              )(<.TextField(^.source := "slug")()),
              <.TextField(^.source := "country", ^.sortable := false)(),
              <.ReferenceField(
                ^.source := "tagTypeId",
                ^.label := "Tag Type",
                ^.reference := Resource.tagType,
                ^.linkType := false,
                ^.allowEmpty := true,
                ^.sortable := false
              )(<.TextField(^.source := "label")()),
              <.NumberField(^.source := "weight")()
            )
          )
        }
      )

  def tagFilters(): ReactElement = {
    <.Filter(^.resource := Resource.tags)(
      Seq(<.TextInput(^.label := "Label", ^.source := "label", ^.alwaysOn := true)()),
      <.ReferenceInput(
        ^.label := "Tag Type",
        ^.source := "tagTypeId",
        ^.reference := Resource.tagType,
        ^.alwaysOn := true
      )(<.SelectInput(^.optionText := "label")()),
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
        ^.alwaysOn := true
      )(<.SelectInput(^.optionText := "slug")()),
      <.SelectInput(
        ^.label := "Country",
        ^.source := "country",
        ^.alwaysOn := true,
        ^.choices := Configuration.choicesCountryFilter
      )()
    )
  }
}
