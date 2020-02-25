package controllers

import models.Product._
import models.JsonFormats.productFormat
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.DocumentServices

import scala.concurrent.Future

/**
 * Created by karim on 9/19/15.
 */
class Product //@Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends Controller //with MongoController with ReactiveMongoComponents
{
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Product])

  def getAll = Action.async { implicit request =>
    DocumentServices.Product.findAll.map{
      list =>
        Ok(Json.toJson(list))
    }
  }

  def get(id: String) = Action.async { implicit request =>
    DocumentServices.Product.get(id).map {
      optItem =>
        optItem match {
          case Some(item) => Ok(Json.toJson(item))
          case None => NoContent
        }
    }
  }

  def create = Action.async(parse.json) { implicit request =>
    request.body.validate[models.Product].map {
      product =>
        DocumentServices.Product.insert(product).map {
          lastError =>
            logger.debug(s"Successfully inserted with LastError: $lastError")
            Created(Json.toJson(product))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
}
