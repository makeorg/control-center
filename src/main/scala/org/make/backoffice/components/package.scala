package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import org.make.backoffice.components.aor.validatedProposals.ValidatedProposalList

package object components {
  implicit class RichVirtualDOMElements(val self: VirtualDOMElements) extends AnyVal {
    def ShowProposalTitle: ReactClassElementSpec = self(proposal.ShowProposalTitle.reactClass)
    def ShowProposalComponents: ReactClassElementSpec = self(proposal.ShowProposalComponents.reactClass)
    def FormRefuseProposalComponent: ReactClassElementSpec = self(proposal.FormRefuseProposalComponent.reactClass)
    def FormValidateProposalComponent: ReactClassElementSpec = self(proposal.FormValidateProposalComponent.reactClass)
    def FormPostponeProposalComponent: ReactClassElementSpec = self(proposal.FormPostponeProposalComponent.reactClass)
    def ModerationHistoryComponent: ReactClassElementSpec = self(proposal.ModerationHistoryComponent.reactClass)
    def SimilarProposalsComponent: ReactClassElementSpec = self(proposal.SimilarProposalsComponent.reactClass)
    def SimilarClusterComponent: ReactClassElementSpec = self(proposal.SimilarClusterComponent.reactClass)
    def StatsValidatedProposal: ReactClassElementSpec = self(proposal.StatsValidatedProposal.reactClass)
    def ActionComponent: ReactClassElementSpec = self(ValidatedProposalList.Action.reactClass)
    def ExportComponent: ReactClassElementSpec = self(ValidatedProposalList.ExportComponent.reactClass)
  }
}
