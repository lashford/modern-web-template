package models

import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by karim on 9/16/15.
 */
case class Tax (_id: String, name: String, percent: Double) {
  def copy(_id: String) = Tax(_id, this.name, this.percent)
}

object Tax {
  val fldId = "_id"
  val fldName = "name"
  val fldPercent = "percent"

  implicit object TaxBSONWriter extends BSONDocumentWriter[Tax] {
    def write(t: Tax) = BSONDocument(
      fldId -> {
        if (t._id.isEmpty)
          BSONObjectID.generate
        else
          BSONObjectID(t._id)
      },
      fldName -> t.name,
      fldPercent -> t.percent
    )
  }

  implicit object TaxBSONReader extends BSONDocumentReader[Tax] {
    def read(d: BSONDocument) = Tax(
      d.getAs[BSONObjectID](fldId).get.stringify,
      d.getAs[String](fldName).get,
      d.getAs[Double](fldPercent).get
    )
  }
}