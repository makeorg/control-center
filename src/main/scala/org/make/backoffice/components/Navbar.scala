package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

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
