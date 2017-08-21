package org.make.client

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@js.native
sealed trait Response extends js.Object

@js.native
trait ListTotalResponse[ENTITY <: js.Object] extends js.Object with Response {
  val data: js.Array[ENTITY]
  val total: Int
}
object ListTotalResponse {
  def apply[ENTITY <: js.Object](data: Seq[ENTITY]): ListTotalResponse[ENTITY] =
    js.Dynamic.literal(data = data.toJSArray, total = data.size).asInstanceOf[ListTotalResponse[ENTITY]]
}

@js.native
trait ListDataResponse[ENTITY <: js.Object] extends js.Object with Response {
  val data: js.Array[ENTITY]
}
object ListDataResponse {
  def apply[ENTITY <: js.Object](data: Seq[ENTITY]): ListDataResponse[ENTITY] =
    js.Dynamic.literal(data = data.toJSArray).asInstanceOf[ListDataResponse[ENTITY]]
}

@js.native
trait SingleResponse[+ENTITY <: js.Object] extends js.Object with Response {
  val data: ENTITY
}
object SingleResponse {
  def apply[ENTITY <: js.Object](data: ENTITY): SingleResponse[ENTITY] =
    js.Dynamic.literal(data = data).asInstanceOf[SingleResponse[ENTITY]]
}
