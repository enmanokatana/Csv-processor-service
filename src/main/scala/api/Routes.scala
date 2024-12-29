package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import com.typesafe.scalalogging.LazyLogging
import config.ApiConfig
import model._
import play.api.libs.json._
import service.CsvProcessor

import scala.util.{Failure, Success}
import java.io.File

class Routes(config: ApiConfig) extends LazyLogging {
  private val csvProcessor = new CsvProcessor()

  val routes = {
    pathPrefix("api") {
      path("process") {
        get {
          val inputDir = new File(config.inputDirectory)
          if (!inputDir.exists()) {
            complete(StatusCodes.InternalServerError -> "Input directory not found")
          } else {
            val files = inputDir.listFiles().filter(_.getName.endsWith(".csv"))
            val results = files.map { file =>
              csvProcessor.processFile(file) match {
                case Success(result) =>
                  // Move processed file
                  val targetDir = if (result.errorCount == 0) {
                    new File(config.processedDirectory)
                  } else {
                    new File(config.errorDirectory)
                  }

                  targetDir.mkdirs()
                  val targetFile = new File(targetDir, file.getName)
                  file.renameTo(targetFile)

                  result

                case Failure(e) =>
                  ProcessingResult(
                    filename = file.getName,
                    recordsProcessed = 0,
                    successCount = 0,
                    errorCount = 1,
                    errors = Seq(e.getMessage)
                  )
              }
            }

            complete(StatusCodes.OK -> Json.toJson(results).toString())
          }
        }
      }
    }
  }
}