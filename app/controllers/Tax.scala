package controllers

import javax.inject.Inject

import org.slf4j.{LoggerFactory, Logger}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{ReactiveMongoComponents, MongoController, ReactiveMongoApi}
import services.DocumentServices
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.json._
import models.JsonFormats.taxFormat

import models.Tax._

/**
 * Created by karim on 9/22/15.
 */
class Tax extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Tax])

  def getAll = Action.async { request =>
    DocumentServices.Tax.findAll.map {
      list =>
        Ok(Json toJson list)
    }
  }

  def get(id: String) = Action.async { request =>
    DocumentServices.Tax.get(id).map {
      optTax =>
        optTax match {
          case Some(tax) => Ok(Json toJson tax)
          case None => NoContent
        }
    }
  }

  def create = Action.async(parse.json) { request =>
    request.body.validate[models.Tax].map {
      tax =>
        DocumentServices.Tax.insert(tax).map {
          lastError =>
            logger.debug(s"Successfully inserted Tax with LastError: $lastError")
            Created(Json.toJson(tax))
        }
    }.getOrElse(Future successful BadRequest("invalid json"))
  }
}
