package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.statictags.{Attribute, SpaceSeparatedStringAttributeSpec}
import org.make.backoffice.component.proposal.validated.ValidatedProposalList
import scalacss.DevDefaults._

package object component {
  implicit class RichVirtualDOMElements(val self: VirtualDOMElements) extends AnyVal {
    def ShowProposalTitle: ReactClassElementSpec = self(proposal.common.ShowProposalTitle.reactClass)
    def ShowProposalComponents: ReactClassElementSpec = self(proposal.common.ShowProposalComponents.reactClass)
    def FormRefuseProposalComponent: ReactClassElementSpec =
      self(proposal.common.FormRefuseProposalComponent.reactClass)
    def FormValidateProposalComponent: ReactClassElementSpec =
      self(proposal.common.FormValidateProposalComponent.reactClass)
    def FormPostponeProposalComponent: ReactClassElementSpec =
      self(proposal.common.FormPostponeProposalComponent.reactClass)
    def ModerationHistoryComponent: ReactClassElementSpec = self(proposal.common.ModerationHistoryComponent.reactClass)
    def ProposalIdeaComponent: ReactClassElementSpec = self(proposal.common.ProposalIdeaComponent.reactClass)
    def StatsValidatedProposal: ReactClassElementSpec = self(proposal.common.ValidatedProposalStats.reactClass)
    def ActionComponent: ReactClassElementSpec = self(ValidatedProposalList.Action.reactClass)
    def ExportComponent: ReactClassElementSpec = self(ValidatedProposalList.ExportComponent.reactClass)
    def NewIdeaComponent: ReactClassElementSpec = self(proposal.common.NewIdeaComponent.reactClass)
    def IdeaTitle: ReactClassElementSpec = self(idea.EditIdea.ideaTitle)
    def CustomDatagrid: ReactClassElementSpec = self(idea.EditIdea.dataGrid)
    def StartModeration: ReactClassElementSpec = self(proposal.moderation.StartModeration.reactClass)
  }

  implicit class RichSpaceSeparatedStringAttributeSpec(val spec: SpaceSeparatedStringAttributeSpec) extends AnyVal {
    def :=(style: StyleA): Attribute[Iterable[String]] = spec := style.htmlClass

    def :=(styleSeq: Seq[StyleA]): Attribute[Iterable[String]] = spec := styleSeq.map(_.htmlClass)
  }
}
