package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._

object Menu {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) => <.ul(^.className := "nav nav-sidebar")(
      <.li()(
        <.Link(^.to := "/")("Home")
      ),
      <.li()(
        <.Link(^.to := "/propositions")("Proposition")
      )
    )
  )
}
