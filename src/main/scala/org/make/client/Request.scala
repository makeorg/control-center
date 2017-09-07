package org.make.client

import org.make.services.proposal.ProposalService

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

case class InvalidRestVerbException(message: String = "Invalid REST verb") extends Exception(message)
case class UnknownResourceException(message: String = "Unknown resource") extends Exception(message)
case class ResourceNotImplementedException(message: String = "Resource not implemented") extends Exception(message)

@js.native
sealed trait Request extends js.Object

object Request {
  def fetch(restVerb: String, resource: String, params: js.Object): Future[Response] =
    restVerb match {
      case "GET_LIST"            => GetListRequest.fetch(resource, params)
      case "GET_ONE"             => GetOneRequest.fetch(resource, params)
      case "CREATE"              => CreateRequest.fetch(resource, params)
      case "UPDATE"              => UpdateRequest.fetch(resource, params)
      case "DELETE"              => DeleteRequest.fetch(resource, params)
      case "GET_MANY"            => GetManyRequest.fetch(resource, params)
      case "GET_MANY_REFERENCES" => GetManyReferenceRequest.fetch(resource, params)
      case invalidRestVerb       => throw InvalidRestVerbException(s"Invalid REST verb: $invalidRestVerb")
    }
}

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

@js.native
trait GetListRequest extends js.Object with Request {
  val pagination: js.UndefOr[Pagination]
  val sort: js.UndefOr[Sort]
  val filter: js.UndefOr[js.Dictionary[js.Any]]
}
object GetListRequest {
  def apply(pagination: Option[Pagination] = None,
            sort: Option[Sort] = None,
            filter: Option[Seq[Filter]] = None): GetListRequest =
    js.Dynamic
      .literal(pagination = pagination.orUndefined, sort = sort.orUndefined, filter = filter.orUndefined)
      .asInstanceOf[GetListRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case "proposals" =>
        val request: GetListRequest = params.asInstanceOf[GetListRequest]
        ProposalService.proposalService.proposals(
          request.pagination.toOption,
          request.sort.toOption,
          request.filter.toOption.map(_.toJSArray.map(fieldValue => Filter(fieldValue._1, fieldValue._2)))
        )
      case "users" =>
        throw ResourceNotImplementedException("Resource users not implemented for request GetListRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource")
    }
  }
}

@js.native
trait GetOneRequest extends js.Object with Request {
  val id: String
}
object GetOneRequest {
  def apply(id: String): GetOneRequest =
    js.Dynamic.literal(id = id).asInstanceOf[GetOneRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case "proposals" =>
        val request = params.asInstanceOf[GetOneRequest]
        ProposalService.proposalService.getProposalById(request.id)
      case "users" =>
        throw ResourceNotImplementedException("Resource users not implemented for request GetOneRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource")
    }
  }
}

@js.native
trait CreateRequest[ENTITY <: js.Object] extends js.Object with Request {
  val data: ENTITY
}
object CreateRequest {
  def apply[ENTITY <: js.Object](data: ENTITY): CreateRequest[ENTITY] =
    js.Dynamic.literal(data = data).asInstanceOf[CreateRequest[ENTITY]]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case "proposals" =>
        throw ResourceNotImplementedException("Resource proposals not implemented for request CreateRequest")
      case "users" =>
        throw ResourceNotImplementedException("Resource users not implemented for request CreateRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource")
    }
  }
}

@js.native
trait UpdateRequest[ENTITY <: js.Object] extends js.Object with Request {
  val id: String
  val data: ENTITY
}
object UpdateRequest {
  def apply[ENTITY <: js.Object](id: String, data: ENTITY): UpdateRequest[ENTITY] =
    js.Dynamic.literal(id = id, data = data).asInstanceOf[UpdateRequest[ENTITY]]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case "proposals" =>
        throw ResourceNotImplementedException("Resource proposals not implemented for request UpdateRequest")
      case "users" =>
        throw ResourceNotImplementedException("Resource users not implemented for request UpdateRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource")
    }
  }
}

@js.native
trait DeleteRequest extends js.Object with Request {
  val id: String
}
object DeleteRequest {
  def apply(id: String): DeleteRequest =
    js.Dynamic.literal(id = id).asInstanceOf[DeleteRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case "proposals" =>
        throw ResourceNotImplementedException("Resource proposals not implemented for request DeleteRequest")
      case "users" =>
        throw ResourceNotImplementedException("Resource users not implemented for request DeleteRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource")
    }
  }
}

@js.native
trait GetManyRequest extends js.Object with Request {
  val id: Seq[String]
}
object GetManyRequest {
  def apply(id: Seq[String]): GetManyRequest =
    js.Dynamic.literal(id = id).asInstanceOf[GetManyRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case "proposals" =>
        throw ResourceNotImplementedException("Resource proposals not implemented for request GetManyRequest")
      case "users" =>
        throw ResourceNotImplementedException("Resource users not implemented for request GetManyRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource")
    }
  }
}

@js.native
trait GetManyReferenceRequest extends js.Object with Request {
  val target: String
  val id: String
  val pagination: js.UndefOr[Pagination]
  val sorts: js.UndefOr[Seq[Sort]]
  val filter: js.UndefOr[Seq[Filter]]
}
object GetManyReferenceRequest {
  def apply(target: String,
            id: String,
            pagination: Option[Pagination] = None,
            sorts: Option[Seq[Sort]] = None,
            filter: Option[Seq[Filter]] = None): GetManyReferenceRequest =
    js.Dynamic
      .literal(
        target = target,
        id = id,
        pagination = pagination.orUndefined,
        sorts = sorts.orUndefined,
        filter = filter.orUndefined
      )
      .asInstanceOf[GetManyReferenceRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case "proposals" =>
        throw ResourceNotImplementedException("Resource proposals not implemented for request GetManyReferenceRequest")
      case "users" =>
        throw ResourceNotImplementedException("Resource users not implemented for request GetManyReferenceRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource")
    }
  }
}
