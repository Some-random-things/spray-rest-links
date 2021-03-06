package com.imilkaeu.links.rest

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import com.imilkaeu.links.database.{DatabaseRequester, LinkDAO}
import java.text.{ParseException, SimpleDateFormat}
import java.util.Date
import net.liftweb.json.Serialization._
import net.liftweb.json.{DateFormat, Formats}
import scala.Some
import spray.http._
import spray.routing._

/**
 * REST Service actor.
 */
class RestServiceActor extends Actor with RestService {

  implicit def actorRefFactory = context

  def receive = runRoute(rest)
}

/**
 * REST Service
 */
trait RestService extends HttpService with SLF4JLogging {

  val databaseRequester = new DatabaseRequester(actorRefFactory)
  val linksDao = new LinkDAO

  implicit val executionContext = actorRefFactory.dispatcher

  implicit val liftJsonFormats = new Formats {
    val dateFormat = new DateFormat {
      val sdf = new SimpleDateFormat("yyyy-MM-dd")

      def parse(s: String): Option[Date] = try {
        Some(sdf.parse(s))
      } catch {
        case e: Exception => None
      }

      def format(d: Date): String = sdf.format(d)
    }
  }

  implicit val customRejectionHandler = RejectionHandler {
    case rejections => mapHttpResponse {
      response =>
        response.withEntity(HttpEntity(ContentType(MediaTypes.`application/json`),
          write(Map("error" -> response.entity.asString))))
    } {
      RejectionHandler.Default(rejections)
    }
  }

  val rest = respondWithMediaType(MediaTypes.`application/json`) {
    pathPrefix("api") {
      path("links") {
        linksApi
      }
    }
  }

  def linksApi = {
    get {
      parameters('query, 'leftProperties, 'rightProperties) { (query, leftProperties, rightProperties) =>
        complete(databaseRequester.linksRequest(query, leftProperties.toString.split(",").toList, rightProperties.toString.split(",").toList))
      }
    }
  }
}