package com.test.example.controllers

import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.unmarshalling.PredefinedFromStringUnmarshallers
import com.test.example.service.ModelService
import com.test.example.utils.{ApiUtils, Enities}

class ModelController(modelService: ModelService = Enities.modelService) extends Directives with PredefinedFromStringUnmarshallers {

  case class ProgramIdBody(programId: Int)

  def createModels: Route = {
    post {
      entity(as[ProgramIdBody]) { programIdBody =>
        val result = modelService.getOrCreateModelsByProgramId(programIdBody.programId)

        onSuccess(result.value) { either =>
          ApiUtils.convert(either)
        }
      }
    }
  }

  val routes = {
    pathPrefix( "models") {
      pathEndOrSingleSlash {
        createModels
      }
    }
  }

}
