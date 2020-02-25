package models

import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by karim on 9/16/15.
 */
case class Product (_id: String, name: String, price: Double, tax: Option[String])

object Product {
  val fldId = "_id"
  val fldName = "name"
  val fldPrice = "price"
  val fldTax = "tax"
  
  implicit object ProductServiceBSONWriter extends BSONDocumentWriter[Product] {
    def write(p: Product) = BSONDocument(
      fldId -> {
        if (p._id.isEmpty)
          BSONObjectID.generate
        else
          BSONObjectID(p._id)
      },
      fldName -> p.name,
      fldPrice -> p.price,
      fldTax -> p.tax.getOrElse("")
    )
  }

  implicit object ProductServiceBSONReader extends BSONDocumentReader[Product] {
    def read(d: BSONDocument) = Product(
      _id = d.getAs[BSONObjectID](fldId).get.stringify,
      name = d.getAs[String](fldName).get,
      price = d.getAs[Double](fldPrice).get,
      tax = {
        val tax = d.getAs[String](fldTax).get
        if (tax.isEmpty)
          None
        else
          Some(tax)
      }
    )
  }
}