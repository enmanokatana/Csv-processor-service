package model

import play.api.libs.json.{Format, Json}

case class SalesRecord(
                        id: String,
                        date: String,
                        product: String,
                        quantity: Int,
                        unitPrice: BigDecimal,
                        totalAmount: BigDecimal
                      )

object SalesRecord {
  implicit val format: Format[SalesRecord] = Json.format[SalesRecord]
}

case class ProcessingResult(
                             filename: String,
                             recordsProcessed: Int,
                             successCount: Int,
                             errorCount: Int,
                             errors: Seq[String]
                           )

object ProcessingResult {
  implicit val format: Format[ProcessingResult] = Json.format[ProcessingResult]
}