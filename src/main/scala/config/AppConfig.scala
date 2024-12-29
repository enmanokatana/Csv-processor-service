package config


import com.typesafe.config.ConfigFactory
import scala.util.Try

case class ApiConfig(
                      host: String,
                      port: Int,
                      inputDirectory: String,
                      processedDirectory: String,
                      errorDirectory: String
                    )

object AppConfig {
  def load(): Try[ApiConfig] = Try {
    val config = ConfigFactory.load()
    ApiConfig(
      host = config.getString("api.host"),
      port = config.getInt("api.port"),
      inputDirectory = config.getString("api.csv.input-directory"),
      processedDirectory = config.getString("api.csv.processed-directory"),
      errorDirectory = config.getString("api.csv.error-directory")
    )
  }
}