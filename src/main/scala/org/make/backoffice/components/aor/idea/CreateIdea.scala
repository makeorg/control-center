package org.make.backoffice.components.aor.idea

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Create._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.SimpleForm._
import org.make.backoffice.facades.Choice
import org.make.backoffice.helpers.Configuration
import org.make.client.Resource

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object CreateIdea {

  case class CreateProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateProps, Unit](
        displayName = "CreateIdea",
        render = (self) => {

          // toDo: get from configuration when available
          val countryChoices: js.Array[Choice] = Seq(
            Choice(id = "FR", name = "France"),
            Choice(id = "IT", name = "Italy"),
            Choice(id = "GB", name = "United Kingdom")
          ).toJSArray
          val languagesByCountry: Map[String, js.Array[Choice]] = Map(
            "FR" -> Seq(Choice(id = "fr", name = "French")).toJSArray,
            "IT" -> Seq(Choice(id = "it", name = "Italian")).toJSArray,
            "GB" -> Seq(Choice(id = "en", name = "English")).toJSArray
          )

          <.Create(^.resource := Resource.ideas, ^.location := self.props.location)(
            <.SimpleForm()(
              <.TextInput(^.source := "name", ^.allowEmpty := false, ^.options := Map("fullWidth" -> true))(),
              <.SelectInput(^.source := "country", ^.choices := countryChoices, ^.allowEmpty := false)(),
              languagesByCountry.map {
                case (country, languages) =>
                  <.DependentInput(^.dependsOn := "country", ^.dependsValue := country)(
                    <.SelectInput(^.source := "language", ^.choices := languages, ^.allowEmpty := false)()
                  )
              },
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
              <.TextInput(^.source := "question", ^.options := Map("fullWidth" -> true))()
            )
          )
        }
      )
}
