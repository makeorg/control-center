package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.backoffice.libs.Proposition
import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js


object ShowProposition extends RouterProps {

  case class State(proposition: Option[Proposition])

  def apply(): ReactClass = React.createClass[Unit, State](
    getInitialState = (_) => State(None),
    componentDidMount = (self) => {
      Ajax.get("http://localhost:3000/propositions/" + self.props.`match`.params("id")).map{ xhr =>
        val json = js.JSON.parse(xhr.responseText).asInstanceOf[Proposition]
        self.setState(State(proposition = Option(json)))
      }
    },
    render = (self) => {
      if (self.state.proposition.nonEmpty)
        <.div(^.className := "row")(
          <.h2()("Moderation de la proposition"),
          <.div(^.className := "text-left")(
            "Identifiant: " + self.state.proposition.get.id
          ),
          <.div(^.className := "text-left")(
            "Contenu: " + self.state.proposition.get.content
          ),
          <.div(^.className := "text-left")(
            "Auteur: " + self.state.proposition.get.author
          ),
          <.div(^.className := "text-left")(
            "Status: " + self.state.proposition.get.status
          ),
          <.div(^.className := "text-left")(
            <.button(^.className := "btn btn-success")("Activer")
          ),
          <.div(^.className := "text-left")(
            <.button(^.className := "btn btn-danger")("Refuser")
          ),
          <.div(^.className := "text-left")(
            <.button(^.className := "btn btn-info")("Marquer comme doublon")
          ),
          <.div(^.className := "row")(
            <.Link(^.to := "/propositions")("Retour")
          )
        )
      else
        <.div(^.className := "row")(
          <.div(^.className := "alert alert-danger", ^.role := "alert")("Erreur: Proposition pas trouvee"),
          <.div(^.className := "row")(
            <.Link(^.to := "/propositions")("Retour")
          )
        )
    }
  )
}
