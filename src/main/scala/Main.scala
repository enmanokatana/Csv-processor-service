
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import config.AppConfig
import api.Routes
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App with LazyLogging {
  implicit val system = ActorSystem(Behaviors.empty, "DataProcessingSystem")

  AppConfig.load() match {
    case Success(config) =>
      val routes = new Routes(config)

      Http().newServerAt(config.host, config.port)
        .bind(routes.routes)
        .map(_ => logger.info(s"Server online at http://${config.host}:${config.port}/"))
        .recover {
          case ex =>
            logger.error("Failed to start server", ex)
            system.terminate()
        }

    case Failure(ex) =>
      logger.error("Failed to load configuration", ex)
      system.terminate()
  }
}
