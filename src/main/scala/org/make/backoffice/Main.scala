package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js.JSApp

/**
  * Created by cpestoury on 29/05/2017.
  */
object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")

    //val store = Redux.createStore(Reducer.reduce)

    /*
     * Import the following to access the Provider:
     *
     * - import io.github.shogowada.scalajs.reactjs.VirtualDOM._
     * - import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
     * */
    ReactDOM.render(
      <(Homepage()).empty,
      mountNode
    )
  }
}

object Homepage {

  def apply(): ReactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div()(
        <(Navbar()).empty,
        <.div(^.className := "container-fluid")(
          <.div(^.className := "row")(
            <(Container()).empty)
        )
      )
  )
}

object Navbar {

  def apply(): ReactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.nav(^.className  := "navbar navbar-inverse navbar-fixed-top")(
        <.div(^.className := "container-fluid")(
          <.div(^.className := "navbar-header")(
            <.button(^.`type` := "button", ^.className := "navbar-toggle collapsed", ^("data-toogle") := "collapse")(
              <.span(^.className := "sr-only")("Toogle navigation"),
              <.span(^.className := "icon-bar")(),
              <.span(^.className := "icon-bar")(),
              <.span(^.className := "icon-bar")()
            ),
            <.a(^.className := "navbar-brand", ^.href := "#", ^.rel := "nofollow")(
              <.img(^.src := "https://cdn.make.org/Main/_img/device/favicon-32x32.png")()
            )
          ),
          <.div(^.id := "navbar", ^.className := "navbar-collapse collapse")(
            <.ul(^.className := "nav navbar-nav navbar-left")(
              <.li()(<.a(^.href := "#")("Propositions"))
            ),
            <.ul(^.className := "nav navbar-nav navbar-right")(
              <.li()(<.a(^.href := "#")("Sign In"))
            )
          )
        )
      )
  )
}

object Container {

  def apply(): ReactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div(^.className := "main")(
        <.h1()("Make.org - Backoffice")
      )
  )
}