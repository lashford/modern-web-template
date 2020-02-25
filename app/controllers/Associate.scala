package controllers

import javax.inject.Inject

import org.slf4j.{LoggerFactory, Logger}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{ReactiveMongoComponents, MongoController, ReactiveMongoApi}
import reactivemongo.bson.BSONObjectID
import services.DocumentServices
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.json._
import models.JsonFormats.associateFormat

import models.Associate._
/**
 * Created by karim on 9/19/15.
 */
class Associate //@Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends Controller //with MongoController with ReactiveMongoComponents
{
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Associate])

  def getAll = Action.async { implicit request =>
    DocumentServices.Associate.findAll.map{
      list =>
        Ok(Json.toJson(list))
    }
  }

  def get(id: String) = Action.async { implicit request =>
    DocumentServices.Associate.get(id).map {
      optItem =>
        optItem match {
          case Some(item) => Ok(Json.toJson(item))
          case None => NoContent
        }
    }
  }

  def create = Action.async(parse.json) { implicit request =>
    request.body.validate[models.Associate].map {
      associate =>
        DocumentServices.Associate.insert(associate).map {
          lastError =>
            logger.debug(s"Successfully inserted with LastError: $lastError")
            Created(Json.toJson(associate))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
}
