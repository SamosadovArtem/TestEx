package com.test.example.utils

import com.test.example.daos.ModelDao
import com.test.example.service.ModelService

sealed trait ServiceError {
  val statusCode: Int
  val errorCode: Option[String]
  val message: String
  val details: Option[Map[String, String]]
}

case class DataAssertionViolation(message: String, details: Option[Map[String, String]] = None) extends ServiceError {

  val statusCode: Int = 500
  val errorCode = Some("ERR_UNKNOWN")
}

case class ModelsCreated(validModelExternalIds: List[Int])
case class ModelValidateResult(modelId: Int, validateResult: Boolean)


object Enities {
  val modelDaO = new ModelDao()
  val modelService = new ModelService(modelDaO)
}
