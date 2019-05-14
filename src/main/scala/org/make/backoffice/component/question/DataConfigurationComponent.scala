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
                                    testedProposalsEngagementThreshold: Double,
                                    testedProposalsScoreThreshold: Double,
                                    testedProposalsControversyThreshold: Double,
                                    testedProposalsMaxVotesThreshold: Int,
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
        testedProposalsEngagementThreshold = dataConfiguration.testedProposalsEngagementThreshold,
        testedProposalsScoreThreshold = dataConfiguration.testedProposalsScoreThreshold,
        testedProposalsControversyThreshold = dataConfiguration.testedProposalsControversyThreshold,
        testedProposalsMaxVotesThreshold = dataConfiguration.testedProposalsMaxVotesThreshold,
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
            testedProposalsEngagementThreshold = 0,
            testedProposalsScoreThreshold = 0,
            testedProposalsControversyThreshold = 0,
            testedProposalsMaxVotesThreshold = 0,
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
            self.setState(_.copy(testedProposalsMaxVotesThreshold = value))
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
            self.setState(_.copy(testedProposalsEngagementThreshold = value))
          }

          def handleTestedProposalsScoreThresholdEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value.toDouble
            self.setState(_.copy(testedProposalsScoreThreshold = value))
          }

          def handleTestedProposalsControversyThresholdEdition: FormSyntheticEvent[HTMLInputElement] => Unit = {
            event =>
              val value = event.target.value.toDouble
              self.setState(_.copy(testedProposalsControversyThreshold = value))
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

          def onClickUpdateDataConfiguration: SyntheticEvent => Unit = {
            event =>
              {
                event.preventDefault()
                val request = DataConfiguration(
                  newProposalsRatio = self.state.newProposalsRatio,
                  newProposalsVoteThreshold = self.state.newProposalsVoteThreshold,
                  testedProposalsEngagementThreshold = self.state.testedProposalsEngagementThreshold,
                  testedProposalsScoreThreshold = self.state.testedProposalsScoreThreshold,
                  testedProposalsControversyThreshold = self.state.testedProposalsControversyThreshold,
                  testedProposalsMaxVotesThreshold = self.state.testedProposalsMaxVotesThreshold,
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
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Tested proposals max votes threshold",
              ^.value := self.state.testedProposalsMaxVotesThreshold,
              ^.fullWidth := true,
              ^.onChange := handleTestedProposalsMaxVotesThresholdEdition
            )(),
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
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Tested proposals engagement threshold",
              ^.value := self.state.testedProposalsEngagementThreshold,
              ^.fullWidth := true,
              ^.onChange := handleTestedProposalsEngagementThresholdEdition
            )(),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Tested proposals score threshold",
              ^.value := self.state.testedProposalsScoreThreshold,
              ^.fullWidth := true,
              ^.onChange := handleTestedProposalsScoreThresholdEdition
            )(),
            <.TextFieldMaterialUi(
              ^.`type` := "number",
              ^.floatingLabelText := "Tested proposals controversy threshold",
              ^.value := self.state.testedProposalsControversyThreshold,
              ^.fullWidth := true,
              ^.onChange := handleTestedProposalsControversyThresholdEdition
            )(),
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
            <.FlatButton(
              ^.label := "Update data configuration",
              ^.onClick := onClickUpdateDataConfiguration,
              ^.fullWidth := true,
              ^.secondary := true
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
