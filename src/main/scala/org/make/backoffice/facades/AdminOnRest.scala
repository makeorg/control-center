package org.make.backoffice.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "default")
object JsonServerRestClient extends js.Object {
  def jsonServerRestClient(apiUrl: String): js.Dynamic = js.native
}

@js.native
@JSImport("admin-on-rest", "ViewTitle")
object NativeViewTitle extends ReactClass

object ViewTitle {
  implicit class ViewTitleVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ViewTitle: ReactClassElementSpec = elements(NativeViewTitle)
  }

  implicit class ViewTitleVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val title = StringAttributeSpec("title")
  }
}

case class ReactClassAttributeSpec(name: String) extends AttributeSpec {
  def :=(element: ReactClass): Attribute[ReactClass] =
    Attribute(name = name, value = element, AS_IS)
}

case class ElementAttributeSpec(name: String) extends AttributeSpec {
  def :=(element: ReactElement): Attribute[ReactElement] =
    Attribute(name = name, value = element, AS_IS)
}

case class MapAttributeSpec(name: String) extends AttributeSpec {
  def :=(sort: Map[String, _]): Attribute[Map[String, _]] =
    Attribute(name = name, value = sort, AS_IS)
}

case class MatchAttributeSpec(name: String) extends AttributeSpec {
  def :=(value: Match): Attribute[Match] =
    Attribute(name = name, value = value, AS_IS)
}

case class ChoicesAttributeSpec(name: String) extends AttributeSpec {
  def :=(value: js.Array[Choice]): Attribute[js.Array[Choice]] =
    Attribute(name = name, value = value, AS_IS)
}

@js.native
trait Params extends js.Object {
  val id: String
}

object Params {
  def apply(id: String): Params =
    js.Dynamic.literal(id = id).asInstanceOf[Params]
}

@js.native
trait Match extends js.Object {
  val params: Params
}

object Match {
  def apply(params: Params): Match = js.Dynamic.literal(params = params).asInstanceOf[Match]
}

@js.native
trait Choice extends js.Object {
  val id: String
  val name: String
}

object Choice {
  def apply(id: String, name: String): Choice =
    js.Dynamic.literal(id = id, name = name).asInstanceOf[Choice]
}
