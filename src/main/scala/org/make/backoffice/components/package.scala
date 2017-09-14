package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec

package object components {
  implicit class RichVirtualDOMElements(val self: VirtualDOMElements) extends AnyVal {
    def FormRefuseProposalComponent: ReactClassElementSpec = self(proposal.FormRefuseProposalComponent.reactClass)
    def FormValidateProposalComponent: ReactClassElementSpec = self(proposal.FormValidateProposalComponent.reactClass)
    def ShowProposalComponents: ReactClassElementSpec = self(proposal.ShowProposalComponents.reactClass)
    def ModerationHistoryComponent: ReactClassElementSpec = self(proposal.ModerationHistoryComponent.reactClass)
  }
}
