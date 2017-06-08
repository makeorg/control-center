package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterReduxAction.Push
import org.make.backoffice.models.GlobalState

object BreadcrumbsController extends RouterProps {
  private lazy val reactClass = ReactRedux.connectAdvanced { dispatch: Dispatch =>
    val routeProposition = () => dispatch(Push("/propositions"))
    val routeHomepage = () => dispatch(Push("/"))
    (state: GlobalState, ownProps: Props[Unit]) => {
      BreadcrumbsPresentational.Props(
        path = ownProps.location.pathname
      )
    }
  }(BreadcrumbsPresentational())

  def apply(): ReactClass = reactClass
}

object BreadcrumbsPresentational {

  case class Props(path: String)

  private lazy val reactClass = React.createClass[Props, Unit] { self =>
    val breadcrumbsPath = if (self.props.wrapped.path.startsWith("/home"))
      self.props.wrapped.path
    else
      "/home" + self.props.wrapped.path
    val breadcrumbs: Seq[String] = breadcrumbsPath.split("/").dropRight(1)
    val active = breadcrumbsPath.split("/").last

    <.ol(^.className := "breadcrumb")(
      breadcrumbs.map { bc =>
        breadcrumb(bc)
      },
      <.li(^.className := "active")(active.capitalize)
    )
  }

  def breadcrumb(bcName: String): ReactElement =
    <.li()(<.a(^.href := "#")(bcName.capitalize))

  def apply(): ReactClass = reactClass

}