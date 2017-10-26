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

  case class FormProps(proposal: SingleProposal, action: String)
  case class FormState(content: String,
                       maxLength: Int,
                       labels: Seq[String] = Seq.empty,
                       notifyUser: Boolean = true,
                       theme: Option[ThemeId] = None,
                       operation: Option[String] = None,
                       tags: Seq[Tag] = Seq.empty,
                       errorMessage: Option[String] = None,
                       similarProposals: Seq[String] = Seq.empty)

  def getTagFromThemeIdAndTagId(themeId: Option[ThemeId], tagId: TagId): Option[Tag] = {
    themeId.map { themeId =>
      Configuration.getTagsFromThemeId(themeId).find(_.tagId.value == tagId.value)
    }.getOrElse(Configuration.getTagsFromVFF.find(_.tagId.value == tagId.value))
  }

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[FormProps, FormState](
          displayName = "FormValidateProposalComponent",
          getInitialState = { self =>
            FormState(
              content = self.props.wrapped.proposal.content,
              maxLength =
                Configuration.businessConfig.map(_.proposalMaxLength).getOrElse(Configuration.defaultProposalMaxLength),
              labels = self.props.wrapped.proposal.labels,
              theme = self.props.wrapped.proposal.theme.toOption,
              operation = self.props.wrapped.proposal.context.operation.toOption,
              tags = self.props.wrapped.proposal.tags.toSeq.map { tagId =>
                getTagFromThemeIdAndTagId(self.props.wrapped.proposal.theme.toOption, tagId)
                  .getOrElse(Tag(TagId(""), ""))
              }
            )
          },
          componentWillReceiveProps = { (self, props) =>
            self.setState(
              _.copy(
                labels = props.wrapped.proposal.labels,
                theme = props.wrapped.proposal.theme.toOption,
                operation = props.wrapped.proposal.context.operation.toOption,
                tags = props.wrapped.proposal.tags.toSeq.map { tagId =>
                  getTagFromThemeIdAndTagId(props.wrapped.proposal.theme.toOption, tagId).getOrElse(Tag(TagId(""), ""))
                }
              )
            )
          },
          render = { self =>
            def handleContentEdition: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
              val newContent: String = event.target.value.substring(0, self.state.maxLength)
              self.setState(_.copy(content = newContent))
            }

            def handleThemeChange: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
              val theme = Some(ThemeId(value))
              self.setState(_.copy(theme = theme, tags = Seq.empty))
            }

            def handleTagChange: (js.Object, js.UndefOr[Int], js.Array[String]) => Unit = {
              (_, _, values) =>
                val tags: Seq[Tag] = values.toSeq.map { value =>
                  val tagId = self.state.theme.flatMap { themeId =>
                    val taglist = Configuration.getTagsFromThemeId(themeId)
                    taglist.find(tag => tag.label == value).map(_.tagId.value)
                  }.getOrElse(
                    Configuration.getTagsFromVFF.find(tag => tag.label == value).map(_.tagId.value).getOrElse("")
                  )
                  Tag(tagId = TagId(tagId), label = value)
                }
                self.setState(_.copy(tags = tags))
            }

            def handleNotifyUserChange: (js.Object, Boolean) => Unit = { (_, checked) =>
              self.setState(_.copy(notifyUser = checked))
            }

            def handleLabelSelection: (FormSyntheticEvent[HTMLInputElement], Boolean) => Unit = { (event, _) =>
              val label: String = event.target.value
              val newLabels: Seq[String] =
                if (self.state.labels.contains(label)) {
                  self.state.labels.filter(_ != label)
                } else {
                  self.state.labels :+ label
                }
              self.setState(_.copy(labels = newLabels))
            }

            def handleSubmitUpdate: (SyntheticEvent) => Unit = {
              event =>
                event.preventDefault()
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                proposalService
                  .updateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = mayBeNewContent,
                    labels = self.state.labels,
                    theme = self.state.theme,
                    tags = self.state.tags.map(_.tagId),
                    similarProposals = self.state.similarProposals.map(ProposalId.apply)
                  )
                  .onComplete {
                    case Success(_) =>
                      self.props.history.push("/validated_proposals")
                      self.setState(_.copy(errorMessage = None))
                    case Failure(_) =>
                      self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
                  }
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
                    case Failure(_) =>
                      self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
                  }
            }

            def handleSubmit: (SyntheticEvent) => Unit = {
              if (self.props.wrapped.action == "validate")
                handleSubmitValidate
              else
                handleSubmitUpdate
            }

            val selectTheme = <.SelectField(
              ^.disabled := self.state.operation.nonEmpty,
              ^.floatingLabelText := "Theme",
              ^.floatingLabelFixed := true,
              ^.value := self.state.theme.map(_.value).getOrElse("Select a theme"),
              ^.onChangeSelect :=
                handleThemeChange
            )(Configuration.businessConfig.map { bc =>
              bc.themes.map { theme =>
                <.MenuItem(
                  ^.key := theme.themeId.value,
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
              ^.floatingLabelText := "Tags",
              ^.floatingLabelFixed := true,
              ^.valueSelect := self.state.tags.map(_.label),
              ^.onChangeMultipleSelect := handleTagChange
            )(tags.map { tag =>
              <.MenuItem(
                ^.key := tag.tagId.value,
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

            <.Card(^.style := Map("marginTop" -> "1em"))(
              <.CardTitle(^.title := s"I want to ${self.props.wrapped.action} this proposal")(),
              <.CardActions()(
                <.TextFieldMaterialUi(
                  ^.floatingLabelText := "Proposal content",
                  ^.value := self.state.content,
                  ^.onChange := handleContentEdition
                )(),
                <.span()(s"${self.state.content.length}/${self.state.maxLength}"),
                <.br()(),
                selectTheme,
                selectTags,
                <.Checkbox(
                  ^.disabled := self.props.wrapped.action == "update",
                  ^.label := "Notify user",
                  ^.checked := self.state.notifyUser && self.props.wrapped.action == "validate",
                  ^.onCheck := handleNotifyUserChange,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.Checkbox(
                  ^.label := "Local",
                  ^.value := "Local",
                  ^.checked := self.state.labels.contains("Local"),
                  ^.onCheck := handleLabelSelection,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.Checkbox(
                  ^.label := "Action",
                  ^.value := "Action",
                  ^.checked := self.state.labels.contains("Action"),
                  ^.onCheck := handleLabelSelection,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.Checkbox(
                  ^.label := "Star",
                  ^.value := "Star",
                  ^.checked := self.state.labels.contains("Star"),
                  ^.onCheck := handleLabelSelection,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.Card(^.style := Map("marginTop" -> "1em"))(
                  <.CardTitle(^.title := "Similar proposal")(),
                  <.SimilarProposalsComponent(
                    ^.wrapped := SimilarProposalsProps(
                      self.props.wrapped.proposal,
                      setSimilarProposals,
                      self.state.theme,
                      self.state.operation
                    )
                  )()
                ),
                <.RaisedButton(
                  ^.style := Map("marginTop" -> "1em"),
                  ^.label := s"Confirm ${if (self.props.wrapped.action == "validate") "validation" else "changes"}",
                  ^.onClick := handleSubmit
                )(),
                errorMessage
              )
            )
          }
        )
    )
}
