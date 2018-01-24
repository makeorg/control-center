package org.make.client

import org.make.backoffice.facades.Configuration
import org.make.services.idea.IdeaServiceComponent
import org.make.services.operation.OperationServiceComponent
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.user.UserServiceComponent

trait RequestConfiguration {
  def apiBaseUrl: String = Configuration.apiUrl
}

trait MakeServices extends RequestConfiguration {
  lazy val proposalService = ProposalServiceComponent.proposalService
  lazy val userService = UserServiceComponent.userService
  lazy val operationService = OperationServiceComponent.operationService
  lazy val ideaService = IdeaServiceComponent.ideaService
}
