/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package org.make.backoffice.client

object Resource {
  val proposals = "proposals"
  val toEnrichProposals = "to_enrich_proposals"
  val validatedProposals = "validated_proposals"
  val users = "users"
  val operations = "operations"
  val ideas = "ideas"
  val tags = "tags"
  val tagType = "tag-types"
  val questions = "simple_questions"
  val ideaMappings = "idea-mappings"
  val partners = "partners"
  val questionPersonalities = "question-personalities"
  val personalities = "personalities"
  val topIdeas = "top-ideas"
  val personalityRoles = "personality-roles"

  private val resources: Set[String] =
    Set(
      proposals,
      toEnrichProposals,
      validatedProposals,
      users,
      operations,
      ideas,
      tags,
      tagType,
      questions,
      ideaMappings,
      questionPersonalities,
      personalities,
      topIdeas,
      personalityRoles
    )

  def amongst: String => Boolean = { resources.contains }
}
