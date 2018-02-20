package org.make.client

import org.make.backoffice.facades.Configuration
import org.make.services.idea.IdeaServiceComponent
import org.make.services.idea.IdeaServiceComponent.IdeaService
import org.make.services.operation.OperationServiceComponent
import org.make.services.operation.OperationServiceComponent.OperationService
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.proposal.ProposalServiceComponent.ProposalService
import org.make.services.tag.TagServiceComponent
import org.make.services.tag.TagServiceComponent.TagService
import org.make.services.user.UserServiceComponent
import org.make.services.user.UserServiceComponent.UserService

trait RequestConfiguration {
  def apiBaseUrl: String = Configuration.apiUrl
}

trait MakeServices extends RequestConfiguration {
  lazy val proposalService: ProposalService = ProposalServiceComponent.proposalService
  lazy val userService: UserService = UserServiceComponent.userService
  lazy val operationService: OperationService = OperationServiceComponent.operationService
  lazy val ideaService: IdeaService = IdeaServiceComponent.ideaService
  lazy val tagService: TagService = TagServiceComponent.tagService
}
