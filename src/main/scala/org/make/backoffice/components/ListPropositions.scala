package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.backoffice.libs.Proposition
import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

object ListPropositions {

  case class PropositionState(maybePropositions: Seq[Proposition])

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, PropositionState](
    getInitialState = (_) => PropositionState(Seq.empty),
    componentDidMount = (self) => {
      Ajax.get("http://localhost:3000/propositions").map{ xhr =>
        val json = js.JSON.parse(xhr.responseText).asInstanceOf[js.Array[Proposition]]
        self.setState(PropositionState(maybePropositions = json))
      }
    },
    render = (self) => <.div()(
      <.table(^.className := "table table-striped")(
        <.thead()(
          <.tr()(
            <.th()("#"),
            <.th()("Contenu"),
            <.th()("Auteur"),
            <.th()("Status")
          )
        ),
        <.tbody()(
          self.state.maybePropositions map(p => displayProposition(p))
        )
      )
    )
  )

  def displayProposition(p: Proposition): ReactElement = {
    PropositionLink(p.id, p)
  }
}
