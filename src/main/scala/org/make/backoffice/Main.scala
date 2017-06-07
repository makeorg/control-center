package org.make.backoffice

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux._
import org.make.backoffice.components.{BreadcrumbsController, NavbarController, RouteController}
import org.make.backoffice.reducers.Reducer
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
    val history: History = History.createHashHistory()
    val store = Redux.createStore(
      Reducer.reduce,
      ReduxDevTools.composeWithDevTools(
        Redux.applyMiddleware(
          ReactRouterRedux.routerMiddleware(history)
        )
      )
    )
    val wrapperNode = dom.document.getElementById("wrapper")

    ReactDOM.render(
      <.Provider(^.store := store)(
        <.ConnectedRouter(^.history := history)(
          <.div()(
            <.Route(^.component := NavbarController())(),
            <.Route(^.component := BreadcrumbsController())(),
            <.Route(^.component := RouteController())()
          )
        )
      ),
      wrapperNode
    )
  }
}