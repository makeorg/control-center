package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.backoffice.libs.Card._
import org.make.backoffice.libs.ViewTitle._

object Dashboard {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.Card()(
        <.ViewTitle(^.title := "Home")()
      )
  )
}
