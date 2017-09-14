package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.backoffice.models.SingleProposal
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.proposal.ProposalServiceComponent.ProposalService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FormValidateProposalComponent {
  val proposalService: ProposalService = ProposalServiceComponent.proposalService

  case class FormProps(proposal: SingleProposal)
  case class FormState(content: String, labels: Seq[String] = Seq.empty, notifyUser: Boolean = false)

  def handleContentEdition(self: Self[FormProps, FormState]): (FormSyntheticEvent[HTMLInputElement]) => Unit =
    (event: FormSyntheticEvent[HTMLInputElement]) => {
      val newContent = event.target.value
      self.setState(_.copy(content = newContent))
    }

  def handleLabelSelection(self: Self[FormProps, FormState]): (FormSyntheticEvent[HTMLInputElement]) => Unit =
    (event: FormSyntheticEvent[HTMLInputElement]) => {
      val label: String = event.target.value
      val newLabels: Seq[String] =
        if (self.state.labels.contains(label)) {
          self.state.labels.filter(_ != label)
        } else {
          self.state.labels :+ label
        }
      self.setState(_.copy(labels = newLabels))
    }

  def handleSubmitValidate(self: Self[FormProps, FormState]): () => Unit =
    () => {
      val mayBeNewContent =
        if (self.state.content != self.props.wrapped.proposal.content) {
          Some(self.state.content)
        } else { None }
      proposalService
        .validateProposal(
          self.props.wrapped.proposal.id,
          mayBeNewContent,
          self.state.notifyUser,
          labels = self.state.labels
        )
        .onComplete {
          case Success(_) => scalajs.js.Dynamic.global.console.log("validation succeeded")
          case Failure(e) => scalajs.js.Dynamic.global.console.log(s"call failed with error $e")
        }
    }

  lazy val reactClass: ReactClass =
    React.createClass[FormProps, FormState](
      getInitialState = (self) => {
        FormState(content = self.props.wrapped.proposal.content, labels = self.props.wrapped.proposal.labels)
      },
      render = (self) => {
        <.div()(
          <.div()("I want to validate that proposal"),
          <.div()(
            <.input(
              ^.`type`.text,
              ^.label := "content",
              ^.value := self.state.content,
              ^.onChange := handleContentEdition(self)
            )(),
            <.input(^.`type`.checkbox, ^.id := "notify-user", ^.value := "notify-user")(),
            <.label(^.`for` := "notify-user")("Notify user"),
            <.input(
              ^.`type`.checkbox,
              ^.name := "labels-local",
              ^.id := "labels-local",
              ^.value := "Local",
              ^.checked := self.state.labels.contains("Local"),
              ^.onChange := handleLabelSelection(self)
            )(),
            <.label(^.`for` := "labels-local")("Local"),
            <.input(
              ^.`type`.checkbox,
              ^.name := "labels-action",
              ^.id := "labels-action",
              ^.value := "Action",
              ^.checked := self.state.labels.contains("Action"),
              ^.onChange := handleLabelSelection(self)
            )(),
            <.label(^.`for` := "labels-action")("Action"),
            <.input(
              ^.`type`.checkbox,
              ^.name := "labels-star",
              ^.id := "labels-star",
              ^.value := "Star",
              ^.checked := self.state.labels.contains("Star"),
              ^.onChange := handleLabelSelection(self)
            )(),
            <.label(^.`for` := "labels-star")("Star"),
            <.button(^.onClick := handleSubmitValidate(self))("Confirm validation")
          )
        )
      }
    )
}
