package org.make.backoffice.component.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.facade.AdminOnRest.ShowButton._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.client.Resource

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
          )(<.Datagrid()(<.ShowButton()(), <.TextField(^.source := "label")()))
        }
      )

  def tagFilters(): ReactElement = {
    <.Filter(^.resource := Resource.tags)(
      Seq(<.TextInput(^.label := "Label", ^.source := "label", ^.alwaysOn := true)())
    )
  }
}
