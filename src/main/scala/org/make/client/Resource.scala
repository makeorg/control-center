package org.make.client

object Resource {
  val proposals = "proposals"
  val validatedProposals = "validated_proposals"
  val users = "users"

  def amongst: String => Boolean = {
    case `proposals` | `validatedProposals` | `users` => true
    case _                                            => false
  }
}
