package org.make.services.idea

final case class CreateIdeaRequest(name: String,
                                   language: Option[String],
                                   country: Option[String],
                                   operation: Option[String],
                                   question: Option[String])

final case class UpdateIdeaRequest(name: String)
