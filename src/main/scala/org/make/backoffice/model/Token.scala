package org.make.backoffice.model

final case class Token(token_type: String, access_token: String, expires_in: Long, refresh_token: String)
