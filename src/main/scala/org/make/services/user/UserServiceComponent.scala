package org.make.services.user

import org.make.backoffice.models.User
import org.make.client.SingleResponse
import org.make.core.CirceClassFormatters
import org.make.core.URI._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class NoTokenException(message: String = "No token provided") extends Exception(message)

trait UserServiceComponent {
  def userService: UserService = new UserService

  class UserService extends ApiService with CirceClassFormatters {

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
}

object UserServiceComponent extends UserServiceComponent
