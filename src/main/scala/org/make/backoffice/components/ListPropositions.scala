package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

object ListPropositions {

  private lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    (_) => <.h1()("List Propositions")
  )

  def apply(): ReactClass = reactClass
}
