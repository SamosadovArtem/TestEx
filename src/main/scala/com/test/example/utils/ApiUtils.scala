package com.test.example.utils

import akka.http.scaladsl.server.StandardRoute

object ApiUtils {

  def convert[T](result: Either[ServiceError, T]): StandardRoute = ???

}
