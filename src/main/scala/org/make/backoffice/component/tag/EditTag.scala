package org.make.backoffice.component.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.Choice
import org.make.backoffice.model.Tag
import org.make.backoffice.facades.AdminOnRest.required
import org.make.backoffice.util.Configuration

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object EditTag {

  case class TitleState(tag: Tag)

  lazy val tagTitle: ReactClass = React.createClass[Unit, TitleState](getInitialState = self => {
    val tag = self.props.native.record.asInstanceOf[Tag]
    TitleState(tag)
  }, componentWillUpdate = (self, props, _) => {
    val tag = props.native.record.asInstanceOf[Tag]
    self.setState(_.copy(tag))
  }, render = self => {
    <.h1()(self.state.tag.label)
  })

  case class CreateTagProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateTagProps, Unit](
        displayName = "CreateTag",
        render = self => {

          <.Edit(
            ^.resource := Resource.tags,
            ^.location := self.props.location,
            ^.`match` := self.props.`match`,
            ^.editTitle := <.TagTitle()(),
            ^.hasList := true
          )(
            <.SimpleForm()(
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
              )(<.SelectInput(^.optionText := "slug")()),
              <.NumberInput(^.source := "weight", ^.validate := required, ^.allowEmpty := false)()
            )
          )
        }
      )
}
