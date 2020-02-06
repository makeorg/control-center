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

package org.make.backoffice.component.autoComplete

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.facade.DataSourceConfig
import org.make.backoffice.facade.MaterialUi._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object AutoComplete {

  case class AutoCompleteProps(searchRequest: Option[String]      => Future[Seq[js.Object]],
                               handleNewRequest: (js.Object, Int) => Unit,
                               dataSourceConfig: DataSourceConfig)
  case class AutoCompleteState(searchContent: String, searchResults: Seq[js.Object], autoCompleteLoading: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[AutoCompleteProps, AutoCompleteState](displayName = "AutoComplete", getInitialState = { _ =>
        AutoCompleteState(searchContent = "", searchResults = Seq.empty, autoCompleteLoading = false)
      }, render = {
        self =>
          def filterAutoComplete: (String, String) => Boolean = (searchText, key) => {
            key.toLowerCase.indexOf(searchText.toLowerCase) != -1
          }

          def handleUpdateUserInput: (String, js.Array[js.Object], OnUpdateInputParams) => Unit = {
            (searchText, _, params) =>
              self.setState(_.copy(searchContent = searchText, searchResults = Seq.empty, autoCompleteLoading = true))
              if (searchText.length >= 3 && params.source != "click") {
                self.props.wrapped.searchRequest(Some(searchText)).onComplete {
                  case Success(entities) =>
                    if (self.state.searchContent == searchText) {
                      self.setState(_.copy(searchResults = entities, autoCompleteLoading = false))
                    }
                  case Failure(_) =>
                    if (self.state.searchContent == searchText) {
                      self.setState(_.copy(searchResults = Seq.empty, autoCompleteLoading = false))
                    }
                }
              } else {
                self.setState(_.copy(autoCompleteLoading = false))
              }
          }

          <.div()(
            <.AutoComplete(
              ^.id := "search",
              ^.hintText := "Search (write at least 3 letters to start the search)",
              ^.dataSource := self.state.searchResults,
              ^.dataSourceConfig := self.props.wrapped.dataSourceConfig,
              ^.searchText := self.state.searchContent,
              ^.onUpdateInput := handleUpdateUserInput,
              ^.onNewRequest := self.props.wrapped.handleNewRequest,
              ^.fullWidth := true,
              ^.popoverProps := Map("canAutoPosition" -> false),
              ^.openOnFocus := true,
              ^.filterAutoComplete := filterAutoComplete,
              ^.menuProps := Map("maxHeight" -> 400)
            )(), {
              if (self.state.autoCompleteLoading) {
                <.CircularProgress.empty
              }
            }
          )
      })

}
