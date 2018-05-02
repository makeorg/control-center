package org.make.backoffice.component

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.ViewTitle._

object Dashboard {

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React.createClass[Unit, Unit](
      displayName = "Dashboard",
      render = (_) => <.Card()(<.ViewTitle(^.title := "Dashboard")())
    )
}
