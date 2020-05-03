package services

import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, BSONDocument, BSONObjectID}
import scala.concurrent.Future
import reactivemongo.api.collections.bson.BSONCollection


/**
 * Created by karim on 9/19/15.
 */
object DocumentServices {
  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]
  lazy val db = reactiveMongoApi.db

  val fldId = "_id"

  class DocumentModel[T] (val collName: String) {
    val coll = db.collection[BSONCollection](collName)
    def findAll(implicit reader: BSONDocumentReader[T]) = coll.find(BSONDocument()).cursor[T](ReadPreference.primaryPreferred).collect[List]()
    def get(_id: String)(implicit reader: BSONDocumentReader[T]) = coll.find(BSONDocument("_id" -> BSONObjectID(_id))).one[T]
    def insert(t: T)(implicit writer: BSONDocumentWriter[T]) = coll.insert(t)
    def update(_id: String, updateDocument: BSONDocument) = coll.update(BSONDocument(fldId -> BSONObjectID(_id)),updateDocument)
    def remove(_id: String) = coll.remove(BSONDocument("_id" -> BSONObjectID(_id)))
  }

  val Associate = new DocumentModel[models.Associate]("associates")

  val Tax = new DocumentModel[models.Tax] ("tax")

  val Product = new DocumentModel[models.Product] ("product")

  val Discount = new DocumentModel[models.Discount]("discount")

  val Invoice = new DocumentModel[models.Invoice]("invoice")
}
