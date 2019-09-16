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

package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.statictags.{Attribute, SpaceSeparatedStringAttributeSpec}
import scalacss.DevDefaults._

package object component {
  implicit class RichVirtualDOMElements(val self: VirtualDOMElements) extends AnyVal {
    def ShowProposalTitle: ReactClassElementSpec = self(proposal.common.ShowProposalTitle.reactClass)
    def ShowProposalComponents: ReactClassElementSpec = self(proposal.common.ShowProposalComponents.reactClass)
    def FormRefuseProposalComponent: ReactClassElementSpec =
      self(proposal.common.FormRefuseProposalComponent.reactClass)
    def FormEnrichProposalComponent: ReactClassElementSpec =
      self(proposal.common.FormEnrichProposalComponent.reactClass)
    def FormValidateProposalComponent: ReactClassElementSpec =
      self(proposal.common.FormValidateProposalComponent.reactClass)
    def FormPostponeProposalComponent: ReactClassElementSpec =
      self(proposal.common.FormPostponeProposalComponent.reactClass)
    def FormValidateProposalWithTagsComponent: ReactClassElementSpec =
      self(proposal.common.FormValidateProposalWithTagsComponent.reactClass)
    def ModerationHistoryComponent: ReactClassElementSpec = self(proposal.common.ModerationHistoryComponent.reactClass)
    def ProposalIdeaComponent: ReactClassElementSpec = self(proposal.common.ProposalIdeaComponent.reactClass)
    def StatsValidatedProposal: ReactClassElementSpec = self(proposal.common.ValidatedProposalStats.reactClass)
    def NewIdeaComponent: ReactClassElementSpec = self(proposal.common.NewIdeaComponent.reactClass)
    def IdeaTitle: ReactClassElementSpec = self(idea.EditIdea.ideaTitle)
    def TagTitle: ReactClassElementSpec = self(tag.EditTag.tagTitle)
    def CustomIdeaDatagrid: ReactClassElementSpec = self(idea.EditIdea.dataGrid)
    def CustomTagDatagrid: ReactClassElementSpec = self(tag.EditTag.dataGrid)
    def StartModeration: ReactClassElementSpec = self(proposal.moderation.StartModeration.reactClass)
    def StartEnrich: ReactClassElementSpec = self(proposal.toEnrich.StartEnrich.reactClass)
    def StartValidationWithTags: ReactClassElementSpec = self(proposal.moderation.StartValidationWithTags.reactClass)
    def InitialProposal: ReactClassElementSpec = self(question.InitialProposalComponent.reactClass)
    def DataConfigurationComponent: ReactClassElementSpec = self(question.DataConfigurationComponent.reactClass)
    def CreatePartnerComponent: ReactClassElementSpec = self(question.CreatePartnerComponent.reactClass)
    def EditPartnerComponent: ReactClassElementSpec = self(question.EditPartnerComponent.reactClass)
    def DeletePartnerComponent: ReactClassElementSpec = self(question.DeletePartnerComponent.reactClass)
    def HeaderListComponent: ReactClassElementSpec = self(homepage.HeaderListComponent.reactClass)
    def HeaderComponent: ReactClassElementSpec = self(homepage.HeaderComponent.reactClass)
    def CurrentOperationsListComponent: ReactClassElementSpec = self(homepage.CurrentOperationsListComponent.reactClass)
    def ImagePreview: ReactClassElementSpec = self(question.EditQuestion.imagePreview)
    def ImageUrlValidate: ReactClassElementSpec = self(question.EditQuestion.imageUrlValidate)
  }

  implicit class RichSpaceSeparatedStringAttributeSpec(val spec: SpaceSeparatedStringAttributeSpec) extends AnyVal {
    def :=(style: StyleA): Attribute[Iterable[String]] = spec := style.htmlClass

    def :=(styleSeq: Seq[StyleA]): Attribute[Iterable[String]] = spec := styleSeq.map(_.htmlClass)
  }
}
