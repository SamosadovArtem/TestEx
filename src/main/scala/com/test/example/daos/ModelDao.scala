package com.test.example.daos

import scala.concurrent.Future

case class ModelRow(
  id: Int,
  isAvailable: Boolean,
  data: Option[String]
                   )

class ModelDao {

  def getByProgramId(programId: Int): Future[Seq[ModelRow]] = ???

}
