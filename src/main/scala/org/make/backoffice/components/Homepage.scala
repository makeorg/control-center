package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object Homepage {

  def apply(): ReactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div(^.className := "container-fluid")(
        <.div(^.className := "row")(
          <.div(^.className := "main")(
            <.h1()("Make.org - Backoffice")
          )
        )
      )
  )
}
