package org.make.backoffice.libs

import scala.scalajs.js
import scala.scalajs.js.Promise
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("gapi")
object gapi extends js.Object {
  val auth2: Auth2Provider = js.native
  val signin2: Signin2 = js.native
}

@js.native
trait Auth2Provider extends js.Object {
  def getAuthInstance(): Auth2 = js.native
}

@js.native
trait Auth2 extends js.Object {
  def signOut(): Promise[Unit] = js.native
}

@js.native
trait Signin2 extends js.Object {
  def render(method: String, params: js.Dynamic): Nothing = js.native
}