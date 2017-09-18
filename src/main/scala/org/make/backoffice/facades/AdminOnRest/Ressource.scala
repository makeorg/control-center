package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.StringAttributeSpec
import org.make.backoffice.facades.ReactClassAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Resource")
object NativeResource extends ReactClass

object Resource {

  implicit class ResourceVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Resource: ReactClassElementSpec = elements(NativeResource)
  }

  implicit class ResourceVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val name = StringAttributeSpec("name")
    lazy val listing = ReactClassAttributeSpec("list")
    lazy val create = ReactClassAttributeSpec("create")
    lazy val edit = ReactClassAttributeSpec("edit")
    lazy val show = ReactClassAttributeSpec("show")
    lazy val remove = ReactClassAttributeSpec("remove")
  }

}