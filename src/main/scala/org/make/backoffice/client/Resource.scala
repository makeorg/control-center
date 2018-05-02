package org.make.backoffice.client

object Resource {
  val proposals = "proposals"
  val validatedProposals = "validated_proposals"
  val users = "users"
  val operations = "operations"
  val ideas = "ideas"
  val tags = "tags"

  private val resources: Set[String] = Set(proposals, validatedProposals, users, operations, ideas, tags)

  def amongst: String => Boolean = { resources.contains }
}
