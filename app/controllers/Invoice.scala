package controllers

import models.LineItem._
import models.Invoice._
import models.JsonFormats.{lineItemFormat, invoiceFormat}
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONDocument
import services.DocumentServices

import scala.concurrent.Future

/**
 * Created by karim on 9/29/15.
 */
class Invoice extends Controller {
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Invoice])

  def listInvoices = Action.async { request =>
    DocumentServices.Invoice.findAll.map {
      list =>
        Ok(Json.toJson(list map{
          i => models.Invoice(
            i._id,
            i.date,
            i.client,
            i.associate,
            List(),
            i.total
          )}))
    }
  }

  def createInvoice = Action.async(parse.json) { request =>
    request.body.validate[models.Invoice].map {
      invoice =>
        DocumentServices.Invoice.insert(invoice).map{
          lastError =>
            logger.debug(s"Successfully inserted with LastError: $lastError")
            Created(Json toJson invoice)
        }
    }.getOrElse(Future successful BadRequest(s"invalid json"))
  }

  def getInvoice(id: String) = Action.async { request =>
    DocumentServices.Invoice.get(id).map {
      case Some(invoice) => Ok(Json toJson invoice)
      case None => NoContent
    }
  }

  def addLineItem(id: String) = Action.async(parse.json) { request =>
    request.body.validate[models.LineItem].map {
      lineItem =>
        DocumentServices.Invoice.update(id,BSONDocument("$push" -> BSONDocument(models.Invoice.fldLineItems -> lineItem))).map {
          lastError =>
            logger.debug(s"Successfully updated with LastError: $lastError")
            Accepted(Json toJson lineItem)
        }
    }.getOrElse(Future successful BadRequest("invalid json"))
  }
}
