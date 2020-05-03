package models

import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by karim on 9/16/15.
 */
case class Discount (_id: String, name: String, discount: Double, isPercent: Boolean, isAfterTax: Boolean)

object Discount {
  val fldId = "_id"
  val fldName = "name"
  val fldDiscount = "discount"
  val fldIsPercent = "isPercent"
  val fldIsAfterTax = "isAfterTax"

  implicit object DiscountBSONWriter extends BSONDocumentWriter[Discount] {
    def write(a: Discount) = BSONDocument(
      fldId -> {
        if (a._id.isEmpty)
          BSONObjectID.generate
        else
          BSONObjectID(a._id)
      },
      fldName -> a.name,
      fldDiscount -> a.discount,
      fldIsPercent -> a.isPercent,
      fldIsAfterTax -> a.isAfterTax
    )
  }

  implicit object DiscountBSONReader extends BSONDocumentReader[Discount] {
    def read(d: BSONDocument) = Discount(
      d.getAs[BSONObjectID](fldId).get.stringify,
      d.getAs[String](fldName).get,
      d.getAs[Double](fldDiscount).get,
      d.getAs[Boolean](fldIsPercent).get,
      d.getAs[Boolean](fldIsAfterTax).get
    )
  }
}