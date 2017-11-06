package org.make.backoffice.models

sealed trait Label {
  def name: String
}

object Label {
  case object Star extends Label {
    override val name: String = "star"
  }

  case object Action extends Label {
    override val name: String = "action"
  }

  case object Local extends Label {
    override val name: String = "local"
  }
}

