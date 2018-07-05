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

package org.make.backoffice.facade

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.{Location, Match}
import io.github.shogowada.statictags._
import org.make.backoffice.client.Response

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "default")
object JsonServerRestClient extends js.Object {
  def jsonServerRestClient(
    apiUrl: String,
    httpClient: js.Function2[String, js.UndefOr[js.Dictionary[String]], Promise[Response]]
  ): js.Function3[String, String, js.Object, Promise[Response]] = js.native
}

@js.native
@JSImport("admin-on-rest", "fetchUtils")
object FetchJson extends js.Object {
  def fetchJson(url: String, options: js.Dictionary[String]): Promise[Response] = js.native
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

case class FunctionAttributeSpec(name: String) extends AttributeSpec {
  def :=(validate: js.Function): Attribute[js.Function] =
    Attribute(name = name, value = validate, AS_IS)
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
