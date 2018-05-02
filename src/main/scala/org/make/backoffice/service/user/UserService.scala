package org.make.backoffice.service.user

import org.make.backoffice.model.User
import org.make.backoffice.client.SingleResponse
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._
import org.make.backoffice.service.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class NoTokenException(message: String = "No token provided") extends Exception(message)

object UserService extends ApiService with CirceClassFormatters {

  override val resourceName: String = "user"

  def getUserById(id: String): Future[Option[User]] =
    client.get[User](resourceName / id).map(Option.apply).recover { case _: Exception => None }

  def loginGoogle(token: String): Future[SingleResponse[User]] = {
    client.authenticateSocial("google", token).flatMap {
      case true  => client.get[User](resourceName / "me").map(SingleResponse.apply)
      case false => throw NoTokenException()
    }
  }
}
