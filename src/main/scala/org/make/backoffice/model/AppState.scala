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

package org.make.backoffice.model

import scala.scalajs.js

@js.native
trait Params extends js.Object {
  val filter: js.Dictionary[String]
  val page: js.Object
  val sort: String
  val order: String
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
trait Values extends js.Object {
  val questionId: js.UndefOr[String]
}

@js.native
trait RecordForm extends js.Object {
  val values: Values
}

@js.native
trait Admin extends js.Object {
  val resources: Resources
}

@js.native
trait Form extends js.Object {
  val `record-form`: js.UndefOr[RecordForm]
}

@js.native
trait AppState extends js.Object {
  val admin: Admin
  val form: Form
}
