package org.make.client

object Resource {
  val proposals = "proposals"
  val validatedProposals = "validated_proposals"
  val users = "users"
  val operations = "operations"
  val ideas = "ideas"

  def amongst: String => Boolean = {
    case `proposals` | `validatedProposals` | `users` | `operations` | `ideas` => true
    case _                                                                     => false
  }
}
