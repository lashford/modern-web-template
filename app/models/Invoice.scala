package models

import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}

/**
 * Created by karim on 9/16/15.
 */
case class LineItem (description: String, unitPrice: Double, qty: Double, lineTotal: Double)
{
  def calcLineTotal = unitPrice * qty
}

case class Invoice (_id: String, date: java.util.Date, client: String, associate: String, lineItems: List[LineItem], total:Double){
  def calcTotal = (0.0 /: lineItems) ((c,l) => c + l.lineTotal)
}

object LineItem {
  val fldDescription = "description"
  val fldUnitPrice = "unitPrice"
  val fldQty = "qty"
  val fldLineTotal = "lineTotal"

  implicit object LineItemBSONWriter extends BSONDocumentWriter[LineItem] {
    def write(l: LineItem) = BSONDocument(
      fldDescription -> l.description,
      fldUnitPrice -> l.unitPrice,
      fldQty -> l.qty,
      fldLineTotal -> l.calcLineTotal
    )
  }

  implicit object LineItemBSONReader extends BSONDocumentReader[LineItem] {
    def read(d: BSONDocument) = LineItem(
      d.getAs[String](fldDescription).get,
      d.getAs[Double](fldUnitPrice).get,
      d.getAs[Double](fldQty).get,
      d.getAs[Double](fldLineTotal).get
    )
  }
}

object Invoice {
  val fldId = "_id"
  val fldDate = "date"
  val fldClient = "client"
  val fldAssociate = "associate"
  val fldLineItems = "lineItems"
  val fldTotal = "total"

  implicit object InvoiceBSONWriter extends BSONDocumentWriter[Invoice] {
    def write(i: Invoice) = BSONDocument(
      fldId -> {
        if (i._id isEmpty)
          BSONObjectID generate
        else
          BSONObjectID(i._id)
      },
      fldDate -> i.date,
      fldClient -> i.client,
      fldAssociate -> i.associate,
      fldLineItems -> i.lineItems,
      fldTotal -> i.calcTotal
    )
  }

  implicit object InvoiceBSONReader extends BSONDocumentReader[Invoice] {
    def read(d: BSONDocument) = Invoice(
      d.getAs[BSONObjectID](fldId).get.stringify,
      d.getAs[java.util.Date](fldDate).get,
      d.getAs[String](fldClient).get,
      d.getAs[String](fldAssociate).get,
      d.getAs[List[LineItem]](fldLineItems).get,
      d.getAs[Double](fldTotal).get
    )
  }
}