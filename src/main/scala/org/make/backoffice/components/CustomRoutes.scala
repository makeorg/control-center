package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import scala.scalajs.js

object CustomRoutes {

  def customRoutes: js.Array[ReactElement] =
    js.Array(
        <.Route(^.exact := true, ^.path := "/", ^.component := Home())()
      )

}
