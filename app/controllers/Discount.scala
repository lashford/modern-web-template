package controllers

import models.Discount._
import models.JsonFormats.discountFormat
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.DocumentServices

import scala.concurrent.Future

/**
 * Created by karim on 9/19/15.
 */
class Discount //@Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends Controller //with MongoController with ReactiveMongoComponents
{
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Discount])

  def getAll = Action.async { implicit request =>
    DocumentServices.Discount.findAll.map{
      list =>
        Ok(Json.toJson(list))
    }
  }

  def get(id: String) = Action.async { implicit request =>
    DocumentServices.Discount.get(id).map {
      optItem =>
        optItem match {
          case Some(item) => Ok(Json.toJson(item))
          case None => NoContent
        }
    }
  }

  def create = Action.async(parse.json) { implicit request =>
    request.body.validate[models.Discount].map {
      discount =>
        DocumentServices.Discount.insert(discount).map {
          lastError =>
            logger.debug(s"Successfully inserted with LastError: $lastError")
            Created(Json.toJson(discount))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
}
