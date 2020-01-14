package com.test.example.service

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import cats.data.EitherT
import cats.implicits._
import com.test.example.daos.{ModelDao, ModelRow}
import com.test.example.utils.{DataAssertionViolation, ModelValidateResult, ModelsCreated, ServiceError}

import scala.concurrent.{ExecutionContext, Future}

class ModelService(modelDao: ModelDao = new ModelDao) {

  implicit val ec = ExecutionContext.global
  implicit val system = ActorSystem("TestSystem")
  implicit val materializer = ActorMaterializer()

  def validateModel(model: ModelRow): EitherT[Future, ServiceError, ModelValidateResult] = {
    EitherT.fromOption(model.data, DataAssertionViolation("Model doesn't have any data",
      Some(Map("modelId" -> model.id.toString))))
  }

  def getOrCreateModelsByProgramId(programId: Int): EitherT[Future, ServiceError, ModelsCreated] = {

    for {
      models <- EitherT.right[ServiceError](modelDao.getByProgramId(programId))
      modelValidateResults <- {
        val f = Source
          .fromIterator(() => models.iterator)
          .filter(_.isAvailable)
          .mapAsync(3) { model =>
            val eitherT = for {
              validateResult <- validateModel(model)
            } yield validateResult

            eitherT.value
          }
          .runWith(Sink.seq)

        EitherT(normalized(f))

      }
    } yield {
      val validModelIds = modelValidateResults.filter(_.validateResult).map(_.modelId)
      ModelsCreated(validModelIds)
    }

  }

  private def normalized[A, B](input: Future[Seq[Either[A,B]]]): Future[Either[A, List[B]]] = {
    input.map(_.foldRight[Either[A, List[B]]](Right(Nil)) {
      (e, acc) => for (xs <- acc; x <- e) yield x :: xs
    })
  }

}
