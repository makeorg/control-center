package org.make.backoffice.component.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Create._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.AdminOnRest.Toolbar._
import org.make.backoffice.facade.AdminOnRest.SaveButton._
import org.make.backoffice.facades.AdminOnRest.required
import org.make.backoffice.util.Configuration

object CreateTag {
  case class CreateTagProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val toolbar: ReactElement =
    <.Toolbar()(
      <.SaveButton(^.label := "Save", ^.redirect := "list", ^.submitOnEnter := true)(),
      <.SaveButton(^.label := "Save and Add", ^.redirect := false, ^.submitOnEnter := false, ^.raised := false)()
    )

  private lazy val reactClass =
    React
      .createClass[CreateTagProps, Unit](
        displayName = "CreateTag",
        render = self => {

          <.Create(^.resource := Resource.tags, ^.location := self.props.location, ^.hasList := true)(
            <.SimpleForm(^.toolbar := toolbar)(
              <.TextInput(
                ^.source := "label",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.SelectInput(
                ^.source := "country",
                ^.choices := Configuration.choicesCountryFilter,
                ^.allowEmpty := false,
                ^.validate := required
              )(),
              Configuration.choiceLanguageFilter.map {
                case (country, languages) =>
                  <.DependentInput(^.dependsOn := "country", ^.dependsValue := country)(
                    <.SelectInput(
                      ^.source := "language",
                      ^.choices := languages,
                      ^.allowEmpty := false,
                      ^.validate := required
                    )()
                  )
              },
              <.ReferenceInput(
                ^.label := "Tag Type",
                ^.source := "tagTypeId",
                ^.reference := Resource.tagType,
                ^.allowEmpty := false
              )(<.SelectInput(^.optionText := "label")()),
              <.SelectInput(
                ^.label := "Theme",
                ^.source := "themeId",
                ^.allowEmpty := true,
                ^.choices := Configuration.choicesThemeFilter
              )(),
              <.ReferenceInput(
                ^.label := "Operation",
                ^.source := "operationId",
                ^.reference := Resource.operations,
                ^.allowEmpty := true
              )(<.SelectInput(^.optionText := "slug")())
            )
          )
        }
      )
}
