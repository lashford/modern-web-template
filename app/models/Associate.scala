package models

import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by karim on 9/16/15.
 */
case class Associate (_id: String, name: String, active: Boolean) {
  def copy(_id: String) = Associate(_id, this.name, this.active)
}

object Associate {
  val fldId = "_id"
  val fldName = "name"
  val fldActive = "active"

  //implicit def write(a: Associate) = AssociateBSONWriter.write(a)
  //implicit def read(b: BSONDocument) = AssociateBSONReader.read(b)

  implicit object AssociateBSONWriter extends BSONDocumentWriter[Associate] {
    def write(a: Associate) = BSONDocument(
      fldId -> {
          if (a._id.isEmpty)
            BSONObjectID.generate
          else
            BSONObjectID(a._id)
        },
      fldName -> a.name,
      fldActive -> a.active
    )
  }

  implicit object AssociateBSONReader extends BSONDocumentReader[Associate] {
    def read(d: BSONDocument) = Associate(
      d.getAs[BSONObjectID](fldId).get.stringify,
      d.getAs[String](fldName).get,
      d.getAs[Boolean](fldActive).get
    )
  }
}