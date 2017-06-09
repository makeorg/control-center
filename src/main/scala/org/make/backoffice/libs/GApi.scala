package org.make.backoffice.libs

import scala.scalajs.js
import scala.scalajs.js.Promise
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("gapi-google", JSImport.Namespace)
object GApi extends js.Object {
  val gapi: GApi = js.native
}

@js.native
trait GApi extends js.Object {
  val auth2: Auth2Provider = js.native
  val signin2: Signin2 = js.native
}

@js.native
trait Auth2Provider extends js.Object {
  def init(params: Map[String, String]): Unit = js.native

  def getAuthInstance(): Auth2 = js.native
}

@js.native
trait Auth2 extends js.Object {
  def signOut(): Promise[Unit] = js.native
}

@js.native
trait Signin2 extends js.Object {
  def render(method: String, params: RenderParameters): Unit = js.native
}

@js.native
trait RenderParameters extends js.Object {
  def scope: String = js.native
  def width: Int = js.native
  def height: Int = js.native
  def longtitle: Boolean = js.native
  def theme: String = js.native
  def onsuccess: () => _ = js.native
}

object RenderParameters {
  def apply(
             scope: String,
             width: Int,
             height: Int,
             longtitle: Boolean,
             theme: String,
             onsuccess: () => _
           ): RenderParameters = js.Dynamic.literal(
    scope = scope,
    width = width,
    height = height,
    longtitle = longtitle,
    theme = theme,
    onsuccess = onsuccess
  ).asInstanceOf[RenderParameters]
}