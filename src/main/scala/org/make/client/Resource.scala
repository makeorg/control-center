package org.make.client

object Resource {
  val proposals = "proposals"
  val validatedProposals = "validated_proposals"
  val users = "users"
  val operations = "operations"
  val ideas = "ideas"
  val tags = "tags"

  def amongst: String => Boolean = {
    case `proposals` | `validatedProposals` | `users` | `operations` | `ideas` | `tags` => true
    case _                                                                              => false
  }
}
