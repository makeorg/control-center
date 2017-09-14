package org.make.client.request

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@js.native
trait Pagination extends js.Object {
  val page: Int
  val perPage: Int
}
object Pagination {
  def apply(page: Int, perPage: Int): Pagination =
    js.Dynamic.literal(page = page, perPage = perPage).asInstanceOf[Pagination]
}

@js.native
sealed trait Order extends js.Object {
  val shortName: String
}
object Order {
  val asc = Order("ASC")
  val desc = Order("DESC")

  val orders: Map[String, Order] = Map(asc.shortName -> asc, desc.shortName -> desc)

  def apply(order: String): Order = js.Dynamic.literal(shortName = order).asInstanceOf[Order]
}

@js.native
trait Sort extends js.Object {
  val field: js.UndefOr[String]
  val order: js.UndefOr[String]
}
object Sort {
  def apply(field: Option[String], order: Option[String]): Sort =
    js.Dynamic.literal(field = field.orUndefined, order = order.orUndefined).asInstanceOf[Sort]
}

@js.native
trait Filter extends js.Object {
  val field: String
  val value: Any
}
object Filter {
  def apply(field: String, value: js.Any): Filter =
    js.Dynamic.literal(field = field, value = value).asInstanceOf[Filter]
}
