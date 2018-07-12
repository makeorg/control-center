package org.make.backoffice.model

import scala.scalajs.js

@js.native
trait Params extends js.Object {
  val filter: js.Dictionary[String]
}

@js.native
trait List extends js.Object {
  val params: Params
}

@js.native
trait Resource extends js.Object {
  val list: List
}

@js.native
trait Resources extends js.Object {
  val proposals: Resource
  val validated_proposals: Resource
  val ideas: Resource
}

@js.native
trait Admin extends js.Object {
  val resources: Resources
}

@js.native
trait AppState extends js.Object {
  val admin: Admin
}
