package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter

object App {
  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div(^.className := "App")(
        <(Header()).empty,
        <.div(^.className := "container-fluid")(
          <.div(^.className := "row")(
            <.div(^.className := "col-sm-3 col-md-2 sidebar")(
              <(Menu()).empty
            ),
            <.div(^.className := "col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2")(
              <(Main()).empty
            )
          )
        )
      )
  )
}
