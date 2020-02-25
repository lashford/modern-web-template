package models

/**
 * Created by karim on 9/16/15.
 */
object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val userFormat = Json.format[User]
  implicit val associateFormat = Json.format[Associate]
  implicit val taxFormat = Json.format[Tax]
  implicit val productFormat = Json.format[Product]
  implicit val discountFormat = Json.format[Discount]
  implicit val lineItemFormat = Json.format[LineItem]
  implicit val invoiceFormat = Json.format[Invoice]
}
