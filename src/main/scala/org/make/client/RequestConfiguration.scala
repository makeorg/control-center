package org.make.client

import org.make.backoffice.facades.Configuration
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.user.UserServiceComponent

trait RequestConfiguration {
  def apiBaseUrl: String = Configuration.apiUrl
}

trait MakeServices extends ProposalServiceComponent with UserServiceComponent with RequestConfiguration
