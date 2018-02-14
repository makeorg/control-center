package org.make.backoffice.components.aor.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Datagrid._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.List._
import org.make.backoffice.facades.AdminOnRest.ShowButton._
import org.make.client.Resource


object TagList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[ListProps, Unit](
        displayName = "TagList",
        render = self => {
          <.List(
            ^.perPage := 50,
            ^.title := "Tags",
            ^.sort := Map("field" -> "label", "order" -> "ASC"),
            ^.location := self.props.location,
            ^.resource := Resource.tags,
            ^.hasCreate := true
          )(
            <.Datagrid()(
              <.ShowButton()(),
              <.TextField(^.source := "label")()
            )
          )
        }
      )
}
