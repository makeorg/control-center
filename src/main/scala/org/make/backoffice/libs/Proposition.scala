package org.make.backoffice.libs

import scala.scalajs.js

case class PropositionListing[T](propositions: Seq[T])

@js.native
trait Proposition extends js.Object {
  def id: String

  def content: String

  def author: String

  def status: String
}
