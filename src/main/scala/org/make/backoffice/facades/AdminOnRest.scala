package org.make.backoffice.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.{Location, Match}
import io.github.shogowada.statictags._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.JSConverters._

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

case class MapBooleanAttributeSpec(name: String) extends AttributeSpec {
  def :=(map: Map[String, Boolean]): Attribute[js.Dictionary[Boolean]] =
    Attribute(name = name, value = map.toJSDictionary, AS_IS)
}

case class MapStringAttributeSpec(name: String) extends AttributeSpec {
  def :=(map: Map[String, String]): Attribute[js.Dictionary[String]] =
    Attribute(name = name, value = map.toJSDictionary, AS_IS)
}

case class MapIntAttributeSpec(name: String) extends AttributeSpec {
  def :=(map: Map[String, Int]): Attribute[js.Dictionary[Int]] =
    Attribute(name = name, value = map.toJSDictionary, AS_IS)
}

case class MapArrayAttributeSpec(name: String) extends AttributeSpec {
  def :=(map: Map[String, Seq[String]]): Attribute[js.Dictionary[js.Array[String]]] =
    Attribute(name = name, value = map.map { case (key, value) => key -> value.toJSArray }.toJSDictionary, AS_IS)
}

case class MatchAttributeSpec(name: String) extends AttributeSpec {
  def :=(value: Match): Attribute[Match] =
    Attribute(name = name, value = value, AS_IS)
}

case class LocationAttributeSpec(name: String) extends AttributeSpec {
  def :=(path: String): Attribute[String] =
    Attribute[String](name = name, value = path)
  def :=(location: Location): Attribute[Location] =
    Attribute[Location](name = name, value = location, AS_IS)
}

case class ChoicesAttributeSpec(name: String) extends AttributeSpec {
  def :=(value: Seq[Choice]): Attribute[js.Array[Choice]] =
    Attribute(name = name, value = value.toJSArray, AS_IS)

  def :=(value: js.Array[Choice]): Attribute[js.Array[Choice]] =
    Attribute(name = name, value = value, AS_IS)
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
