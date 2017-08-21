package org.make.client

import io.circe.Decoder
import org.scalajs.dom.ext.Ajax.InputData

import scala.concurrent.Future

trait HttpClient {
  def baseUrl: String

  def get[ENTITY](apiEndpoint: String, urlParams: Seq[(String, Any)], headers: Map[String, String])(
    implicit decoder: Decoder[ENTITY]
  ): Future[ENTITY]

  def post[ENTITY](apiEndpoint: String, urlParams: Seq[(String, Any)], data: InputData, headers: Map[String, String])(
    implicit decoder: Decoder[ENTITY]
  ): Future[ENTITY]

  def put[ENTITY](apiEndpoint: String,
                  urlParams: Seq[(String, Any)],
                  data: InputData,
                  headers: Map[String, String] = Map.empty)(implicit decoder: Decoder[ENTITY]): Future[ENTITY]

  def patch[ENTITY](apiEndpoint: String,
                    urlParams: Seq[(String, Any)],
                    data: InputData,
                    headers: Map[String, String] = Map.empty)(implicit decoder: Decoder[ENTITY]): Future[ENTITY]

  def delete[ENTITY](apiEndpoint: String,
                     urlParams: Seq[(String, Any)],
                     data: InputData,
                     headers: Map[String, String] = Map.empty)(implicit decoder: Decoder[ENTITY]): Future[ENTITY]
}
