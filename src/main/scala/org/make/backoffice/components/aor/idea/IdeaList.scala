package org.make.backoffice.components.aor.idea

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Datagrid._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Filter._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.List._
import org.make.backoffice.facades.AdminOnRest.EditButton._
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
            ^.title := "Ideas",
            ^.location := self.props.location,
            ^.resource := Resource.ideas,
            ^.hasCreate := true,
            ^.filters := ideaFilters()
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.TextField(^.source := "name")(),
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
    <.Filter(^.resource := Resource.proposals)(
      Seq(
        <.TextInput(^.label := "Name", ^.source := "name")(),
        <.ReferenceInput(^.label := "Operation", ^.source := "operationId", ^.reference := Resource.operations)(
          <.SelectInput(^.optionText := "slug", ^.alwaysOn := false)()
        )
      )
    )
  }

}
