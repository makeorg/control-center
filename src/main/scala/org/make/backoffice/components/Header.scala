package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

import scala.scalajs.js.Dynamic.{global => g}

object Header {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) => <.div(^.className := "App-header")(
      <.img(^.src := "logo.svg", ^.className := "App-logo", ^.alt := "logo")(),
      <.h3()("Phantom Backoffice")
    )
  )

  def onSuccess(): Unit = {
    g.console.log("log")
  }
}

