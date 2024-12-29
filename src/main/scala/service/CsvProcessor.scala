package service

import model._
import com.github.tototoshi.csv._
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CsvProcessor extends LazyLogging {
  private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  def processFile(file: File): Try[ProcessingResult] = {
    logger.info(s"Starting to process file: ${file.getName}")

    try {
      val reader = CSVReader.open(file)
      try {
        val headers = reader.readNext().getOrElse(throw new Exception("Empty CSV file"))
        validateHeaders(headers)

        val records = reader.all()
        val (validRecords, errors) = records.zipWithIndex.partition { case (record, _) =>
          validateRecord(record).isSuccess
        }

        val salesRecords = validRecords.map { case (record, _) =>
          parseSalesRecord(record).get
        }

        val errorMessages = errors.map { case (record, index) =>
          s"Error in row ${index + 2}: ${validateRecord(record).failed.get.getMessage}"
        }

        Success(ProcessingResult(
          filename = file.getName,
          recordsProcessed = records.size,
          successCount = validRecords.size,
          errorCount = errors.size,
          errors = errorMessages
        ))
      } finally {
        reader.close()
      }
    } catch {
      case e: Exception =>
        logger.error(s"Failed to process file ${file.getName}", e)
        Failure(e)
    }
  }

  private def validateHeaders(headers: Seq[String]): Unit = {
    val requiredHeaders = Set("id", "date", "product", "quantity", "unit_price")
    val missing = requiredHeaders -- headers.map(_.toLowerCase.trim).toSet
    if (missing.nonEmpty) {
      throw new Exception(s"Missing required headers: ${missing.mkString(", ")}")
    }
  }

  private def validateRecord(record: Seq[String]): Try[Unit] = Try {
    if (record.length != 5) {
      throw new Exception("Invalid number of columns")
    }

    // Validate ID
    if (record(0).trim.isEmpty) {
      throw new Exception("ID cannot be empty")
    }

    // Validate Date
    Try(LocalDateTime.parse(record(1), dateFormatter)).getOrElse {
      throw new Exception("Invalid date format")
    }

    // Validate Product
    if (record(2).trim.isEmpty) {
      throw new Exception("Product cannot be empty")
    }

    // Validate Quantity
    Try(record(3).toInt).getOrElse {
      throw new Exception("Invalid quantity")
    }

    // Validate Unit Price
    Try(BigDecimal(record(4))).getOrElse {
      throw new Exception("Invalid unit price")
    }
  }

  private def parseSalesRecord(record: Seq[String]): Try[SalesRecord] = Try {
    val quantity = record(3).toInt
    val unitPrice = BigDecimal(record(4))

    SalesRecord(
      id = record(0).trim,
      date = record(1).trim,
      product = record(2).trim,
      quantity = quantity,
      unitPrice = unitPrice,
      totalAmount = quantity * unitPrice
    )
  }
}