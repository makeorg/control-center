package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.backoffice.libs.Proposition

object PropositionLink {

  def apply(key: String, data: Proposition): ReactElement = {
    val trClassName = {
      if (data.status == "VALID") "success"
      else if (data.status == "TO_VALIDATE") "warning"
      else "danger"
    }
    <.tr(^.key := key, ^.className := trClassName)(
      <.td()(
        <.Link(^.to := "/propositions/" + data.id)(data.id)
      ),
      <.td()(data.content),
      <.td()(data.author),
      <.td()(data.status)
    )
  }
}
