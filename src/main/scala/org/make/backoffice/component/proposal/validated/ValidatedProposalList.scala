/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package org.make.backoffice.component.proposal.validated

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.{redux, React}
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.facade.AdminOnRest.ShowButton._
import org.make.backoffice.facade.Choice
import org.make.backoffice.model.{AppState, Proposal, Tag, TagType}
import org.make.backoffice.service.proposal.Accepted
import org.make.backoffice.service.tag.{TagService, TagTypeService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

@js.native
trait FilterValues extends js.Object {
  def theme: js.UndefOr[String]
  def tags: js.UndefOr[js.Array[String]]
  def content: js.UndefOr[String]
  def operationId: js.UndefOr[String]
  def source: js.UndefOr[String]
  def question: js.UndefOr[String]
  def country: js.UndefOr[String]
}

object ValidatedProposalList {

  lazy val ProposalListContainer: ReactClass =
    redux.ReactRedux.connectAdvanced(selectorFactory)(reactClass)

  def selectorFactory: Dispatch => (AppState, Props[Unit]) => ValidatedProposalListProps =
    (_: Dispatch) =>
      (state: AppState, _: Props[Unit]) => {
        ValidatedProposalListProps(filters = state.admin.resources.proposals.list.params.filter.toMap)
    }

  case class ValidatedProposalListProps(filters: Map[String, String]) extends RouterProps

  case class ValidatedProposalListState(tags: Seq[Tag], tagTypes: Seq[TagType])

  private lazy val reactClass: ReactClass =
    React
      .createClass[ValidatedProposalListProps, ValidatedProposalListState](
        displayName = "ValidatedProposalList",
        getInitialState = { _ =>
          ValidatedProposalListState(Seq.empty, Seq.empty)
        },
        componentDidMount = { self =>
          val questionIdFilter: Option[String] = self.props.wrapped.filters.get("questionId")
          if (questionIdFilter.nonEmpty) {
            TagService
              .tags(questionIdFilter, label = None)
              .onComplete {
                case Success(tags) =>
                  self.setState(_.copy(tags = tags))
                case Failure(_) => self.setState(_.copy(tags = Seq.empty))
              }
          } else {
            self.setState(_.copy(tags = Seq.empty))
          }
          TagTypeService.tagTypes.onComplete {
            case Success(tagTypes) =>
              self.setState(_.copy(tagTypes = tagTypes))
            case Failure(_) => self.setState(_.copy(tagTypes = Seq.empty))
          }
        },
        componentWillReceiveProps = { (self, props) =>
          if (self.props.wrapped.filters != props.wrapped.filters) {
            val questionIdFilter: Option[String] = props.wrapped.filters.get("questionId")
            if (questionIdFilter.nonEmpty) {
              TagService
                .tags(questionIdFilter, label = None)
                .onComplete {
                  case Success(tags) => self.setState(_.copy(tags = tags))
                  case Failure(_)    => self.setState(_.copy(Seq.empty))
                }
            } else {
              self.setState(_.copy(tags = Seq.empty))
            }
            TagTypeService.tagTypes.onComplete {
              case Success(tagTypes) =>
                self.setState(_.copy(tagTypes = tagTypes))
              case Failure(_) => self.setState(_.copy(tagTypes = Seq.empty))
            }
          }
        },
        render = { self =>
          val tagsGroupByTagType: Seq[(TagType, Seq[Tag])] = {
            self.state.tags
              .groupBy[String](_.tagTypeId)
              .flatMap {
                case (tagTypeId, tags) => self.state.tagTypes.find(_.id == tagTypeId).map(_ -> tags)
              }
              .toSeq
              .sortBy {
                case (tagType, _) => -1 * tagType.weight
              }
          }

          val tagChoices = tagsGroupByTagType.flatMap {
            case (tagType, tags) =>
              Choice(tagType.id, tagType.label) +: tags.sortBy(-1 * _.weight).map { tag =>
                Choice(tag.id, s"\u00A0\u00A0\u00A0\u00A0\u00A0${tag.label}")
              }
          }

          <.List(
            ^.title := "Validated proposals",
            ^.location := self.props.location,
            ^.resource := Resource.proposals,
            ^.hasCreate := false,
            ^.filters := filterList(tagChoices, self.props.wrapped.filters.get("questionId").isDefined),
            ^.filter := Map("status" -> Accepted.shortName),
            ^.sortList := Map("field" -> "createdAt", "order" -> "DESC")
          )(
            <.Datagrid()(
              <.ShowButton()(),
              <.TextField(^.source := "content")(),
              <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)(),
              <.FunctionField(^.label := "status", ^.sortable := false, ^.render := { record =>
                val proposal = record.asInstanceOf[Proposal]
                if (proposal.toEnrich.getOrElse(true)) {
                  "Accepted"
                } else {
                  "Enriched"
                }
              })(),
              <.TextField(^.source := "author.userType", ^.sortable := false, ^.label := "Author type")(),
              <.ReferenceArrayField(^.label := "Tags", ^.reference := Resource.tags, ^.source := "tagIds")(
                <.SingleFieldList()(<.ChipField(^.source := "label")())
              ),
              <.FunctionField(^.label := "Votes", ^.sortable := false, ^.render := { record =>
                Proposal.totalVotes(record.asInstanceOf[Proposal].votes)
              })(),
              <.FunctionField(^.label := "Agreement rate", ^.sortable := false, ^.render := { record =>
                s"${Proposal.voteRate(record.asInstanceOf[Proposal].votes, "agree")}%"
              })(),
              <.FunctionField(^.label := "Emergence rate", ^.sortable := false, ^.render := { record =>
                Proposal.totalVotes(record.asInstanceOf[Proposal].votes)
              })()
            )
          )
        }
      )

  def filterList(tagChoices: Seq[Choice], isQuestionSelected: Boolean): ReactElement = {
    val userTypeChoices =
      Seq(Choice("USER", "User"), Choice("ORGANISATION", "Organisation"), Choice("PERSONALITY", "Personality"))

    <.Filter(^.resource := Resource.proposals)(
      <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
      if (isQuestionSelected) {
        <.SelectInput(
          ^.label := "Tags",
          ^.source := "tagsIds",
          ^.choices := tagChoices,
          ^.alwaysOn := true,
          ^.allowEmpty := true
        )()
      },
      <.ReferenceInput(
        ^.label := "Question",
        ^.translateLabel := ((label: String) => label),
        ^.source := "questionId",
        ^.sortList := Map("field" -> "slug", "order" -> "ASC"),
        ^.reference := Resource.questions,
        ^.alwaysOn := true,
        ^.perPage := 100,
        ^.allowEmpty := true
      )(<.SelectInput(^.optionText := "slug")()),
      <.NullableBooleanInput(^.label := "Initital Proposal", ^.source := "initialProposal", ^.alwaysOn := true)(),
      <.SelectInput(
        ^.label := "User type",
        ^.source := "userType",
        ^.alwaysOn := true,
        ^.allowEmpty := true,
        ^.choices := userTypeChoices
      )()
    )
  }
}
