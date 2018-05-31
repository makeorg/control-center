package org.make.backoffice.component.proposal.moderation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import org.make.backoffice.client.request.Filter
import org.make.backoffice.client.{BadRequestHttpException, NotFoundHttpException}
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.{Country, Operation}
import org.make.backoffice.service.operation.OperationService
import org.make.backoffice.service.proposal.{Pending, ProposalService}
import org.make.backoffice.util.Configuration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object StartModeration {

  final case class StartModerationProps() extends RouterProps
  final case class StartModerationState(operationId: Option[String],
                                        themeId: Option[String],
                                        country: Option[String],
                                        language: Option[String],
                                        operations: Seq[Operation],
                                        countriesList: Seq[String],
                                        languagesList: Seq[String],
                                        snackbarOpen: Boolean = false,
                                        errorMessage: String = "",
                                        proposalsAmount: Int = 0)

  val reactClass: ReactClass =
    WithRouter(
      React.createClass[StartModerationProps, StartModerationState](displayName = "StartModeration", getInitialState = {
        _ =>
          StartModerationState(None, None, None, None, Seq.empty, Seq.empty, Seq.empty)
      }, componentWillMount = { self =>
        loadOperationsAndCounts.onComplete {
          case Success(operations) => self.setState(_.copy(operations = operations))
          case Failure(e)          => js.Dynamic.global.console.log(e.getMessage)
        }
      }, render = {
        self =>
          def onSelectOperation: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            val now = new js.Date()
            self.setState(_.copy(operationId = Some(value), themeId = None, country = None, language = None))
            self.state.operations.find(_.id == value).foreach { operation =>
              self.setState(
                _.copy(
                  countriesList = operation.countriesConfiguration
                    .filter(_.endDate.exists(_.toISOString() >= now.toISOString()))
                    .map(_.countryCode)
                )
              )
            }
          }

          def onSelectTheme: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(themeId = Some(value), operationId = None, country = None, language = None))
            Configuration.businessConfig.flatMap(_.themes.find(_.themeId.value == value)).foreach { theme =>
              self.setState(_.copy(countriesList = Seq(theme.country)))
            }
          }

          def onSelectCountry: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(country = Some(value), language = None))
            Configuration.businessConfig
              .flatMap(_.supportedCountries.find(_.countryCode == value))
              .foreach { config =>
                self.setState(_.copy(languagesList = config.supportedLanguages.toSeq))
              }
          }

          def onSelectLanguage: (js.Object, js.UndefOr[Int], String) => Unit = {
            (_, _, value) =>
              self.setState(_.copy(language = Some(value)))
              val filter = self.state.operationId
                .map(operationId => Filter("operationId", operationId))
                .getOrElse(Filter("themeId", self.state.themeId.getOrElse("")))
              ProposalService
                .proposals(
                  None,
                  None,
                  Some(
                    Seq(
                      filter,
                      Filter("country", self.state.country.getOrElse("")),
                      Filter("language", value),
                      Filter("status", s"${Pending.shortName}")
                    )
                  )
                )
                .onComplete {
                  case Success(proposalsTotal) => self.setState(_.copy(proposalsAmount = proposalsTotal.total))
                  case Failure(NotFoundHttpException) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "No proposal found"))
                  case Failure(_) => self.setState(_.copy(snackbarOpen = true, errorMessage = "Internal Error"))
                }
          }

          def onClickStartModeration: SyntheticEvent => Unit = {
            event =>
              event.preventDefault()
              ProposalService
                .nexProposalToModerate(
                  self.state.operationId,
                  self.state.themeId,
                  self.state.country,
                  self.state.language
                )
                .onComplete {
                  case Success(proposal) => self.props.history.push(s"/nextProposal/${proposal.data.id}")
                  case Failure(NotFoundHttpException) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "No proposal found"))
                  case Failure(BadRequestHttpException(_)) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Bad request"))
                  case Failure(_) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Internal Error"))
                }
          }

          <.Card(^.style := Map("marginBottom" -> "3em"))(
            <.CardTitle(^.title := "Moderation")(),
            <.CardHeader(^.title := self.state.proposalsAmount.toString + " proposals to moderate")(),
            <.SelectField(
              ^.style := Map("margin" -> "0 1em"),
              ^.floatingLabelText := "Operation",
              ^.value := self.state.operationId.getOrElse(""),
              ^.onChangeSelect := onSelectOperation
            )(self.state.operations.map { operation =>
              <.MenuItem(^.key := operation.id, ^.value := operation.id, ^.primaryText := operation.slug)()
            }),
            <.SelectField(
              ^.style := Map("margin" -> "0 1em"),
              ^.floatingLabelText := "Theme",
              ^.value := self.state.themeId.getOrElse(""),
              ^.onChangeSelect := onSelectTheme
            )(Configuration.choicesThemeFilter.map { theme =>
              <.MenuItem(^.key := theme.id, ^.value := theme.id, ^.primaryText := theme.name)()
            }),
            <.SelectField(
              ^.style := Map("margin" -> "0 1em"),
              ^.floatingLabelText := "Country",
              ^.value := self.state.country.getOrElse(""),
              ^.onChangeSelect := onSelectCountry
            )(self.state.countriesList.map { country =>
              <.MenuItem(
                ^.key := country,
                ^.value := country,
                ^.primaryText := Country.getCountryNameByCountryCode(country).getOrElse("")
              )()
            }),
            <.SelectField(
              ^.style := Map("margin" -> "0 1em"),
              ^.floatingLabelText := "Language",
              ^.value := self.state.language.getOrElse(""),
              ^.onChangeSelect := onSelectLanguage
            )(self.state.languagesList.map { language =>
              <.MenuItem(^.key := language, ^.value := language, ^.primaryText := language)()
            }),
            <.CardActions()(
              <.RaisedButton(^.label := "Start Moderation", ^.primary := true, ^.onClick := onClickStartModeration)()
            ),
            <.Snackbar(
              ^.open := self.state.snackbarOpen,
              ^.message := self.state.errorMessage,
              ^.autoHideDuration := 5000,
              ^.onRequestClose := (_ => self.setState(_.copy(snackbarOpen = false)))
            )()
          )
      })
    )

  def loadOperationsAndCounts: Future[Seq[Operation]] = {
    OperationService.operations(forceReload = true).map { operationsResponse =>
      operationsResponse.data.filter { operation =>
        val now = new js.Date()
        operation.countriesConfiguration.exists { configuration =>
          configuration.endDate.exists(_.toISOString() >= now.toISOString()) || configuration.endDate.toOption.isEmpty
        }
      }
    }
  }
}
