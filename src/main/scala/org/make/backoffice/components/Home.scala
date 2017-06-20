package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object Home {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) => <.div()(
      <.h1()("Bienvenue, Administrateur, bientôt il y aura un jolie backoffice")
    )
  )
}
