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

package org.make.backoffice.component.proposal.common

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.service.proposal.{Postponed, Refused}

object ShowProposalTitle {

  case class ShowProposalTitleState(proposal: SingleProposal)

  lazy val reactClass: ReactClass =
    React.createClass[Unit, ShowProposalTitleState](displayName = "ShowProposalTitle", getInitialState = { self =>
      val proposal = self.props.native.record.asInstanceOf[SingleProposal]
      ShowProposalTitleState(proposal)
    }, componentWillUpdate = { (self, props, _) =>
      val proposal = props.native.record.asInstanceOf[SingleProposal]
      self.setState(ShowProposalTitleState(proposal))
    }, render = { self =>
      val style: Map[String, _] = self.state.proposal.status match {
        case Postponed.shortName => Map("color" -> "#ffa500")
        case Refused.shortName   => Map("color" -> "#ff3232")
        case _                   => Map.empty
      }
      val propContent: String = self.state.proposal.content
      val propFirstName: Option[String] = self.state.proposal.author.firstName.toOption
      val propAge: Option[Int] = self.state.proposal.author.age.toOption
      val title = (propContent, propFirstName, propAge) match {
        case (content, Some(firstName), Some(age)) => s"$content, $firstName ($age)"
        case (content, Some(firstName), _)         => s"$content, $firstName"
        case (content, _, _)                       => content
      }
      <.h1(^.style := style)(title)
    })

}
