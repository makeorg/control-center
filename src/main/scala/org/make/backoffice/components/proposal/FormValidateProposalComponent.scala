package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.components.proposal.SimilarProposalsComponent.SimilarProposalsProps
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models._
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.proposal.ProposalServiceComponent.ProposalService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object FormValidateProposalComponent {
  val proposalService: ProposalService = ProposalServiceComponent.proposalService

  case class FormProps(proposal: SingleProposal)
  case class FormState(content: String,
                       maxLength: Int,
                       labels: Seq[String] = Seq.empty,
                       notifyUser: Boolean = true,
                       theme: Option[ThemeId] = None,
                       operation: Option[String] = None,
                       tags: Seq[Tag] = Seq.empty,
                       errorMessage: Option[String] = None,
                       similarProposals: Seq[String] = Seq.empty)

  lazy val reactClass: ReactClass =
    WithRouter(React.createClass[FormProps, FormState](getInitialState = { self =>
      FormState(
        content = self.props.wrapped.proposal.content,
        maxLength =
          Configuration.businessConfig.map(_.proposalMaxLength).getOrElse(Configuration.defaultProposalMaxLength),
        labels = self.props.wrapped.proposal.labels,
        theme = self.props.wrapped.proposal.theme.toOption
      )
    }, componentWillReceiveProps = { (self, props) =>
      self.setState(
        _.copy(
          theme = props.wrapped.proposal.theme.toOption,
          operation = props.wrapped.proposal.creationContext.operation.toOption
        )
      )
    }, render = { self =>
      def handleContentEdition: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
        val newContent: String = event.target.value.substring(0, self.state.maxLength)
        self.setState(_.copy(content = newContent))
      }

      def handleThemeChange: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
        val theme = Some(ThemeId(value))
        self.setState(_.copy(theme = theme, tags = Seq.empty))
      }

      def handleTagChange: (js.Object, js.UndefOr[Int], js.Array[String]) => Unit = { (_, _, values) =>
        val tags: Seq[Tag] = values.toSeq.map { value =>
          val tagId = self.state.theme.flatMap { themeId =>
            val taglist = Configuration.getTagsFromThemeId(themeId)
            taglist.find(tag => tag.label == value).map(_.tagId.value)
          }.getOrElse("")
          Tag(tagId = TagId(tagId), label = value)
        }
        self.setState(_.copy(tags = tags))
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

      def handleSubmitValidate: (SyntheticEvent) => Unit = {
        event =>
          event.preventDefault()
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
              theme = self.state.theme,
              similarProposals = self.state.similarProposals.map(ProposalId.apply),
              tags = self.state.tags.map(_.tagId)
            )
            .onComplete {
              case Success(_) =>
                self.props.history.push("/proposals")
                self.setState(_.copy(errorMessage = None))
              case Failure(e) =>
                self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
            }
      }

      val selectTheme = <.SelectField(
        ^.disabled := self.state.operation.nonEmpty || self.props.wrapped.proposal.theme.nonEmpty,
        ^.hintText := "Select a theme",
        ^.value := self.state.theme.map(_.value).getOrElse("Select a theme"),
        ^.onChangeSelect :=
          handleThemeChange
      )(Configuration.businessConfig.map { bc =>
        bc.themes.map { theme =>
          <.MenuItem(
            ^.value := theme.themeId.value,
            ^.primaryText := theme.translations.toArray
              .find(_.language == Configuration.defaultLanguage)
              .map(_.title)
              .getOrElse("")
          )()
        }
      })

      val tags = self.state.theme.map { themeId =>
        Configuration.getTagsFromThemeId(themeId)
      }.getOrElse(Configuration.getTagsFromVFF)

      val selectTags = <.SelectField(
        ^.disabled := self.state.theme.isEmpty && self.state.operation.isEmpty,
        ^.multiple := true,
        ^.hintText := "Select some tags",
        ^.valueSelect := self.state.tags.map(_.label),
        ^.onChangeMultipleSelect := handleTagChange
      )(tags.map { tag =>
        <.MenuItem(
          ^.insetChildren := true,
          ^.checked := self.state.tags.contains(tag),
          ^.value := tag.label,
          ^.primaryText := tag.label
        )()
      })

      val errorMessage: Option[Element] =
        self.state.errorMessage.map(msg => <.p()(msg))

      def setSimilarProposals(similarProposals: Seq[String]): Unit = {
        self.setState(_.copy(similarProposals = similarProposals))
      }

      <.div()(
        <.div()("I want to validate that proposal"),
        <.div()(
          <.input(
            ^.`type`.text,
            ^.label := "content",
            ^.value := self.state.content,
            ^.onChange := handleContentEdition
          )(),
          <.span()(s"${self.state.content.length}/${self.state.maxLength}"),
          selectTheme,
          selectTags,
          <.input(
            ^.`type`.checkbox,
            ^.id := "notify-user-validate",
            ^.value := "notify-user-validate",
            ^.checked := self.state.notifyUser,
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
          <.button(^.onClick := handleSubmitValidate)("Confirm validation"),
          errorMessage
        ),
        <.div()(
          <.SimilarProposalsComponent(
            ^.wrapped := SimilarProposalsProps(self.props.wrapped.proposal, setSimilarProposals)
          )()
        )
      )
    }))
}
