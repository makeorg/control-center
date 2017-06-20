package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.backoffice.components.App
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
//    val history: History = History.createHashHistory()
//    val store = Redux.createStore(
//      Reducer.reduce,
//      ReduxDevTools.composeWithDevTools(
//        Redux.applyMiddleware(
//          ReactRouterRedux.routerMiddleware(history)
//        )
//      )
//    )
    val wrapperNode = dom.document.getElementById("wrapper")

    ReactDOM.render(
      <.BrowserRouter()(
        <(App()).empty
      ),
      wrapperNode
    )
  }
}
