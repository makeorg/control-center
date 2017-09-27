package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.statictags.Element
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.{SingleProposal, ThemeId}
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.proposal.ProposalServiceComponent.ProposalService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FormValidateProposalComponent {
  val proposalService: ProposalService = ProposalServiceComponent.proposalService

  case class FormProps(proposal: SingleProposal)
  case class FormState(content: String,
                       labels: Seq[String] = Seq.empty,
                       notifyUser: Boolean = false,
                       theme: Option[ThemeId] = None)

  lazy val reactClass: ReactClass =
    React.createClass[FormProps, FormState](getInitialState = { self =>
      FormState(content = self.props.wrapped.proposal.content, labels = self.props.wrapped.proposal.labels)
    }, render = {
      self =>
        def handleContentEdition: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
          val newContent = event.target.value
          self.setState(_.copy(content = newContent))
        }

        def handleThemeChange: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
          val themeString = event.target.value
          scalajs.js.Dynamic.global.console.log(s"themeString = $themeString")
          val theme = Some(ThemeId(themeString))
          self.setState(_.copy(theme = theme))
        }

        def handleNotifyUserChange: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
          val notifyUser = event.target.checked
          self.setState(_.copy(notifyUser = notifyUser))
        }

        def handleLabelSelection: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
          val label: String = event.target.value
          val newLabels: Seq[String] =
            if (self.state.labels.contains(label)) {
              self.state.labels.filter(_ != label)
            } else {
              self.state.labels :+ label
            }
          self.setState(_.copy(labels = newLabels))
        }

        def handleSubmitValidate: () => Unit = { () =>
          val mayBeNewContent =
            if (self.state.content != self.props.wrapped.proposal.content) {
              Some(self.state.content)
            } else { None }
          proposalService
            .validateProposal(
              proposalId = self.props.wrapped.proposal.id,
              newContent = mayBeNewContent,
              sendNotificationEmail = self.state.notifyUser,
              labels = self.state.labels,
              theme = self.state.theme
            )
            .onComplete {
              case Success(_) => scalajs.js.Dynamic.global.console.log("validation succeeded")
              case Failure(e) => scalajs.js.Dynamic.global.console.log(s"call failed with error $e")
            }
        }

        val choicesTheme: Seq[Element] = Configuration.choicesThemeFilter.map { themeChoice =>
          <.option(^.value := themeChoice.id)(themeChoice.name)
        }

        val selectTheme = <.select(
          ^.id := "refusal-reason",
          ^.value := self.state.theme.map(_.value).getOrElse(""),
          ^.onChange := handleThemeChange
        )(<.option(^.disabled := true, ^.value := "")("-- select a theme --"), choicesTheme)

        <.div()(
          <.div()("I want to validate that proposal"),
          <.div()(
            <.input(
              ^.`type`.text,
              ^.label := "content",
              ^.value := self.state.content,
              ^.onChange := handleContentEdition
            )(),
            selectTheme,
            <.input(
              ^.`type`.checkbox,
              ^.id := "notify-user-validate",
              ^.value := "notify-user-validate",
              ^.onChange := handleNotifyUserChange
            )(),
            <.label(^.`for` := "notify-user-validate")("Notify user"),
            <.input(
              ^.`type`.checkbox,
              ^.name := "labels-local",
              ^.id := "labels-local",
              ^.value := "Local",
              ^.checked := self.state.labels.contains("Local"),
              ^.onChange := handleLabelSelection
            )(),
            <.label(^.`for` := "labels-local")("Local"),
            <.input(
              ^.`type`.checkbox,
              ^.name := "labels-action",
              ^.id := "labels-action",
              ^.value := "Action",
              ^.checked := self.state.labels.contains("Action"),
              ^.onChange := handleLabelSelection
            )(),
            <.label(^.`for` := "labels-action")("Action"),
            <.input(
              ^.`type`.checkbox,
              ^.name := "labels-star",
              ^.id := "labels-star",
              ^.value := "Star",
              ^.checked := self.state.labels.contains("Star"),
              ^.onChange := handleLabelSelection
            )(),
            <.label(^.`for` := "labels-star")("Star"),
            <.button(^.onClick := handleSubmitValidate)("Confirm validation")
          )
        )
    })
}
