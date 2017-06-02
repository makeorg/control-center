package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.backoffice.components.{Homepage, Navbar}
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
    val headerNode = dom.document.getElementById("header")
    val contentNode = dom.document.getElementById("content")
//    val footerNode = dom.document.getElementById("footer")
    ReactDOM.render(
      <(Navbar()).empty,
      headerNode
    )
    ReactDOM.render(
      <(Homepage()).empty,
      contentNode
    )
  }
}