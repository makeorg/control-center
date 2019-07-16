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

package org.make.backoffice.component.question

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, MouseSyntheticEvent, SyntheticEvent}
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.Question
import org.make.backoffice.model.Question.DataConfiguration
import org.make.backoffice.service.question.QuestionService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object DataConfigurationComponent {

  case class DataConfigurationState(newProposalsRatio: Double,
                                    newProposalsVoteThreshold: Int,
                                    testedProposalsEngagementThreshold: Option[Double],
                                    checkTestedProposalsEngagementThreshold: Boolean,
                                    testedProposalsScoreThreshold: Option[Double],
                                    checkTestedProposalsScoreThreshold: Boolean,
                                    testedProposalsControversyThreshold: Option[Double],
                                    checkTestedProposalsControversyThreshold: Boolean,
                                    testedProposalsMaxVotesThreshold: Option[Int],
                                    checkTestedProposalsMaxVotesThreshold: Boolean,
                                    intraIdeaEnabled: Boolean,
                                    intraIdeaMinCount: Int,
                                    intraIdeaProposalsRatio: Double,
                                    interIdeaCompetitionEnabled: Boolean,
                                    interIdeaCompetitionTargetCount: Int,
                                    interIdeaCompetitionControversialRatio: Double,
                                    interIdeaCompetitionControversialCount: Int,
                                    maxTestedProposalCount: Int,
                                    sequenceSize: Int,
                                    selectionAlgorithmName: String,
                                    snackbarOpen: Boolean,
                                    snackbarMessage: String)

  object DataConfigurationState {
    def apply(dataConfiguration: DataConfiguration): DataConfigurationState = {
      DataConfigurationState(
        newProposalsRatio = dataConfiguration.newProposalsRatio,
        newProposalsVoteThreshold = dataConfiguration.newProposalsVoteThreshold,
        testedProposalsEngagementThreshold = dataConfiguration.testedProposalsEngagementThreshold.toOption,
        checkTestedProposalsEngagementThreshold = dataConfiguration.testedProposalsEngagementThreshold.toOption.nonEmpty,
        testedProposalsScoreThreshold = dataConfiguration.testedProposalsScoreThreshold.toOption,
        checkTestedProposalsScoreThreshold = dataConfiguration.testedProposalsScoreThreshold.toOption.nonEmpty,
        testedProposalsControversyThreshold = dataConfiguration.testedProposalsControversyThreshold.toOption,
        checkTestedProposalsControversyThreshold =
          dataConfiguration.testedProposalsControversyThreshold.toOption.nonEmpty,
        testedProposalsMaxVotesThreshold = dataConfiguration.testedProposalsMaxVotesThreshold.toOption,
        checkTestedProposalsMaxVotesThreshold = dataConfiguration.testedProposalsMaxVotesThreshold.toOption.nonEmpty,
        intraIdeaEnabled = dataConfiguration.intraIdeaEnabled,
        intraIdeaMinCount = dataConfiguration.intraIdeaMinCount,
        intraIdeaProposalsRatio = dataConfiguration.intraIdeaProposalsRatio,
        interIdeaCompetitionEnabled = dataConfiguration.interIdeaCompetitionEnabled,
        interIdeaCompetitionTargetCount = dataConfiguration.interIdeaCompetitionTargetCount,
        interIdeaCompetitionControversialRatio = dataConfiguration.interIdeaCompetitionControversialRatio,
        interIdeaCompetitionControversialCount = dataConfiguration.interIdeaCompetitionControversialCount,
        maxTestedProposalCount = dataConfiguration.maxTestedProposalCount,
        sequenceSize = dataConfiguration.sequenceSize,
        selectionAlgorithmName = dataConfiguration.selectionAlgorithmName,
        snackbarOpen = false,
        snackbarMessage = ""
      )
    }
  }

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, DataConfigurationState](
        displayName = "DataConfiguration",
        getInitialState = { _ =>
          DataConfigurationState(
            newProposalsRatio = 0,
            newProposalsVoteThreshold = 0,
            testedProposalsEngagementThreshold = None,
            checkTestedProposalsEngagementThreshold = true,
            testedProposalsScoreThreshold = None,
            checkTestedProposalsScoreThreshold = true,
            testedProposalsControversyThreshold = None,
            checkTestedProposalsControversyThreshold = true,
            testedProposalsMaxVotesThreshold = None,
            checkTestedProposalsMaxVotesThreshold = true,
            intraIdeaEnabled = false,
            intraIdeaMinCount = 0,
            intraIdeaProposalsRatio = 0,
            interIdeaCompetitionEnabled = false,
            interIdeaCompetitionTargetCount = 0,
            interIdeaCompetitionControversialRatio = 0,
            interIdeaCompetitionControversialCount = 0,
            maxTestedProposalCount = 0,
            sequenceSize = 0,
            selectionAlgorithmName = "",
            snackbarOpen = false,
            snackbarMessage = ""
          )
        },
        componentDidMount = { self =>
          val sequenceId = self.props.native.record.asInstanceOf[Question].landingSequenceId
          sequenceId.foreach { id =>
            QuestionService.getDataConfiguration(id).onComplete {
              case Success(config) => self.setState(DataConfigurationState(config))
              case Failure(_)      =>
            }
          }
        },
        render = { self =>
          def handleSequenceSizeEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value: Int = event.target.value.toInt
            self.setState(_.copy(sequenceSize = value))
          }

          def handleMaxTestedProposalCount: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value: Int = event.target.value.toInt
            self.setState(_.copy(maxTestedProposalCount = value))
          }

          def handleAlgorithmNameEdition: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(selectionAlgorithmName = value))
          }

          def handleTestedProposalsMaxVotesThresholdEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toInt
            self.setState(_.copy(testedProposalsMaxVotesThreshold = Some(value)))
          }

          def handleNewProposalsRatioEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toDouble
            self.setState(_.copy(newProposalsRatio = value))
          }

          def handleNewProposalsVoteThresholdEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toInt
            self.setState(_.copy(newProposalsVoteThreshold = value))
          }

          def handleTestedProposalsEngagementThresholdEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toDouble
            self.setState(_.copy(testedProposalsEngagementThreshold = Some(value)))
          }

          def handleTestedProposalsScoreThresholdEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toDouble
            self.setState(_.copy(testedProposalsScoreThreshold = Some(value)))
          }

          def handleTestedProposalsControversyThresholdEdition: FormSyntheticEvent[HTMLInputElement] => Unit = {
            event =>
              val value = event.target.value.toDouble
              self.setState(_.copy(testedProposalsControversyThreshold = Some(value)))
          }

          def handleIntraIdeaEnabledEdition: MouseSyntheticEvent => Unit = { _ =>
            self.setState(_.copy(intraIdeaEnabled = !self.state.intraIdeaEnabled))
          }

          def handleIntraIdeaMinCountEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toInt
            self.setState(_.copy(intraIdeaMinCount = value))
          }

          def handleIntraIdeaProposalsRatioEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toDouble
            self.setState(_.copy(intraIdeaProposalsRatio = value))
          }

          def handleInterIdeaCompetitionEnabledEdition: MouseSyntheticEvent => Unit = { _ =>
            self.setState(_.copy(interIdeaCompetitionEnabled = !self.state.interIdeaCompetitionEnabled))
          }

          def handleInterIdeaCompetitionTargetCountEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toInt
            self.setState(_.copy(interIdeaCompetitionTargetCount = value))
          }

          def handleInterIdeaCompetitionControversialRatioEdition: FormSyntheticEvent[HTMLInputElement] => Unit = {
            event =>
              val value = event.target.value.toDouble
              self.setState(_.copy(interIdeaCompetitionControversialRatio = value))
          }

          def handleInterIdeaCompetitionControversialCountEdition: FormSyntheticEvent[HTMLInputElement] => Unit = {
            event =>
              val value = event.target.value.toInt
              self.setState(_.copy(interIdeaCompetitionControversialCount = value))
          }

          def handleCheckboxTestedProposalsEngagementThreshold: MouseSyntheticEvent => Unit = { _ =>
            self.setState(
              _.copy(checkTestedProposalsEngagementThreshold = !self.state.checkTestedProposalsEngagementThreshold)
            )
          }

          def handleCheckboxTestedProposalsMaxVotesThreshold: MouseSyntheticEvent => Unit = { _ =>
            self.setState(
              _.copy(checkTestedProposalsMaxVotesThreshold = !self.state.checkTestedProposalsMaxVotesThreshold)
            )
          }

          def handleCheckboxTestedProposalsScoreThreshold: MouseSyntheticEvent => Unit = { _ =>
            self.setState(_.copy(checkTestedProposalsScoreThreshold = !self.state.checkTestedProposalsScoreThreshold))
          }

          def handleCheckboxTestedProposalsControversyThreshold: MouseSyntheticEvent => Unit = { _ =>
            self.setState(
              _.copy(checkTestedProposalsControversyThreshold = !self.state.checkTestedProposalsControversyThreshold)
            )
          }

          def onClickUpdateDataConfiguration: SyntheticEvent => Unit = {
            event =>
              {
                event.preventDefault()

                val testedProposalsEngagementThreshold = if (self.state.checkTestedProposalsEngagementThreshold) {
                  self.state.testedProposalsEngagementThreshold
                } else {
                  None
                }
                val testedProposalsScoreThreshold = if (self.state.checkTestedProposalsScoreThreshold) {
                  self.state.testedProposalsScoreThreshold
                } else {
                  None
                }

                val testedProposalsControversyThreshold = if (self.state.checkTestedProposalsControversyThreshold) {
                  self.state.testedProposalsControversyThreshold
                } else {
                  None
                }

                val testedProposalsMaxVotesThreshold = if (self.state.checkTestedProposalsMaxVotesThreshold) {
                  self.state.testedProposalsMaxVotesThreshold
                } else {
                  None
                }

                val request = DataConfiguration(
                  newProposalsRatio = self.state.newProposalsRatio,
                  newProposalsVoteThreshold = self.state.newProposalsVoteThreshold,
                  testedProposalsEngagementThreshold = testedProposalsEngagementThreshold,
                  testedProposalsScoreThreshold = testedProposalsScoreThreshold,
                  testedProposalsControversyThreshold = testedProposalsControversyThreshold,
                  testedProposalsMaxVotesThreshold = testedProposalsMaxVotesThreshold,
                  intraIdeaEnabled = self.state.intraIdeaEnabled,
                  intraIdeaMinCount = self.state.intraIdeaMinCount,
                  intraIdeaProposalsRatio = self.state.intraIdeaProposalsRatio,
                  interIdeaCompetitionEnabled = self.state.interIdeaCompetitionEnabled,
                  interIdeaCompetitionTargetCount = self.state.interIdeaCompetitionTargetCount,
                  interIdeaCompetitionControversialRatio = self.state.interIdeaCompetitionControversialRatio,
                  interIdeaCompetitionControversialCount = self.state.interIdeaCompetitionControversialCount,
                  maxTestedProposalCount = self.state.maxTestedProposalCount,
                  sequenceSize = self.state.sequenceSize,
                  selectionAlgorithmName = self.state.selectionAlgorithmName
                )
                val sequenceId = self.props.native.record.asInstanceOf[Question].landingSequenceId.getOrElse("")
                val questionId = self.props.native.record.asInstanceOf[Question].id
                QuestionService.putDataConfiguration(sequenceId, questionId, request).onComplete {
                  case Success(_) =>
                    self
                      .setState(_.copy(snackbarOpen = true, snackbarMessage = "Successfully update data configuration"))
                  case Failure(_) =>
                    self.setState(_.copy(snackbarOpen = true, snackbarMessage = "Fail to update data configuration"))
                }
              }
          }

          val testedProposalsMaxVotesThreshold: Int = self.state.testedProposalsMaxVotesThreshold.getOrElse(0)
          val testedProposalsEngagementThreshold: Double = self.state.testedProposalsEngagementThreshold.getOrElse(0)
          val testedProposalsScoreThreshold: Double = self.state.testedProposalsScoreThreshold.getOrElse(0)
          val testedProposalsControversyThreshold: Double = self.state.testedProposalsControversyThreshold.getOrElse(0)

          <.div()(
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Sequence size",
              ^.value := self.state.sequenceSize,
              ^.fullWidth := true,
              ^.onChange := handleSequenceSizeEdition
            )(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Max tested proposal count",
              ^.value := self.state.maxTestedProposalCount,
              ^.fullWidth := true,
              ^.onChange := handleMaxTestedProposalCount
            )(),
            <.SelectField(
              ^.floatingLabelText := "Algorithm name",
              ^.value := self.state.selectionAlgorithmName,
              ^.fullWidth := true,
              ^.onChangeSelect := handleAlgorithmNameEdition
            )(
              <.MenuItem(^.value := "Bandit", ^.primaryText := "Bandit")(),
              <.MenuItem(^.value := "RoundRobin", ^.primaryText := "Round Robin")()
            ),
            <.div(^.style := Map("display" -> "flex"))(
              <.span(^.onClick := handleCheckboxTestedProposalsMaxVotesThreshold)(
                <.Checkbox(
                  ^.style := Map("width" -> "0%"),
                  ^.inputStyle := Map("position" -> "relative"),
                  ^.checked := self.state.checkTestedProposalsMaxVotesThreshold
                )()
              ),
              <.TextFieldMaterialUi(
                ^.`type` := "number",
                ^.floatingLabelText := "Tested proposals max votes threshold",
                ^.value := testedProposalsMaxVotesThreshold,
                ^.fullWidth := true,
                ^.onChange := handleTestedProposalsMaxVotesThresholdEdition,
                ^.disabled := !self.state.checkTestedProposalsMaxVotesThreshold
              )()
            ),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "New proposals ratio",
              ^.value := self.state.newProposalsRatio,
              ^.fullWidth := true,
              ^.onChange := handleNewProposalsRatioEdition
            )(),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "New proposals vote threshold",
              ^.value := self.state.newProposalsVoteThreshold,
              ^.fullWidth := true,
              ^.onChange := handleNewProposalsVoteThresholdEdition
            )(),
            <.div(^.style := Map("display" -> "flex"))(
              <.span(^.onClick := handleCheckboxTestedProposalsEngagementThreshold)(
                <.Checkbox(
                  ^.style := Map("width" -> "0%"),
                  ^.inputStyle := Map("position" -> "relative"),
                  ^.checked := self.state.checkTestedProposalsEngagementThreshold
                )()
              ),
              <.TextFieldMaterialUi(
                ^.`type` := "number",
                ^.floatingLabelText := "Tested proposals engagement threshold",
                ^.value := testedProposalsEngagementThreshold,
                ^.fullWidth := true,
                ^.onChange := handleTestedProposalsEngagementThresholdEdition,
                ^.disabled := !self.state.checkTestedProposalsEngagementThreshold
              )()
            ),
            <.div(^.style := Map("display" -> "flex"))(
              <.span(^.onClick := handleCheckboxTestedProposalsScoreThreshold)(
                <.Checkbox(
                  ^.style := Map("width" -> "0%"),
                  ^.inputStyle := Map("position" -> "relative"),
                  ^.checked := self.state.checkTestedProposalsScoreThreshold
                )()
              ),
              <.TextFieldMaterialUi(
                ^.`type` := "number",
                ^.floatingLabelText := "Tested proposals score threshold",
                ^.value := testedProposalsScoreThreshold,
                ^.fullWidth := true,
                ^.onChange := handleTestedProposalsScoreThresholdEdition,
                ^.disabled := !self.state.checkTestedProposalsScoreThreshold
              )()
            ),
            <.div(^.style := Map("display" -> "flex"))(
              <.span(^.onClick := handleCheckboxTestedProposalsControversyThreshold)(
                <.Checkbox(
                  ^.style := Map("width" -> "0%"),
                  ^.inputStyle := Map("position" -> "relative"),
                  ^.checked := self.state.checkTestedProposalsControversyThreshold
                )()
              ),
              <.TextFieldMaterialUi(
                ^.`type` := "number",
                ^.floatingLabelText := "Tested proposals controversy threshold",
                ^.value := testedProposalsControversyThreshold,
                ^.fullWidth := true,
                ^.onChange := handleTestedProposalsControversyThresholdEdition,
                ^.disabled := !self.state.checkTestedProposalsControversyThreshold
              )()
            ),
            <.span(^.onClick := handleIntraIdeaEnabledEdition)(
              <.Toggle(
                ^.label := "Intra idea enabled",
                ^.toggled := self.state.intraIdeaEnabled,
                ^.style := Map("width" -> "25%"),
                ^.inputStyle := Map("position" -> "relative")
              )()
            ),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Intra idea min count",
              ^.value := self.state.intraIdeaMinCount,
              ^.fullWidth := true,
              ^.onChange := handleIntraIdeaMinCountEdition
            )(),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Intra idea proposals ratio",
              ^.value := self.state.intraIdeaProposalsRatio,
              ^.fullWidth := true,
              ^.onChange := handleIntraIdeaProposalsRatioEdition
            )(),
            <.span(^.onClick := handleInterIdeaCompetitionEnabledEdition)(
              <.Toggle(
                ^.label := "Inter idea competition enabled",
                ^.toggled := self.state.interIdeaCompetitionEnabled,
                ^.style := Map("width" -> "25%"),
                ^.inputStyle := Map("position" -> "relative")
              )()
            ),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Inter idea competition target count",
              ^.value := self.state.interIdeaCompetitionTargetCount,
              ^.fullWidth := true,
              ^.onChange := handleInterIdeaCompetitionTargetCountEdition
            )(),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Inter idea competition controversial Ratio",
              ^.value := self.state.interIdeaCompetitionControversialRatio,
              ^.fullWidth := true,
              ^.onChange := handleInterIdeaCompetitionControversialRatioEdition
            )(),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Inter idea competition controversial count",
              ^.value := self.state.interIdeaCompetitionControversialCount,
              ^.fullWidth := true,
              ^.onChange := handleInterIdeaCompetitionControversialCountEdition
            )(),
            <.RaisedButton(
              ^.label := "Save",
              ^.onClick := onClickUpdateDataConfiguration,
              ^.primary := true,
              ^.style := Map("marginTop" -> "30px")
            )(),
            <.Snackbar(
              ^.open := self.state.snackbarOpen,
              ^.message := self.state.snackbarMessage,
              ^.autoHideDuration := 5000,
              ^.onRequestClose := { _ =>
                self.setState(_.copy(snackbarOpen = false))
              }
            )()
          )
        }
      )

}
