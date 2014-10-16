package com.imilkaeu.links.database

import akka.actor.{ActorRefFactory}
import scala.concurrent.Future
import net.liftweb.json.Serialization._
import net.liftweb.json.{DateFormat, Formats}
import com.imilkaeu.links.domain.{Link, Failure}
import net.liftweb.json.{DateFormat, Formats}
import java.text.SimpleDateFormat
import java.util.Date

case class LinksResponse(query: String, data: List[Link])

class DatabaseRequester(system: ActorRefFactory) {

  import system.dispatcher
  val linkService = new LinkDAO

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

  def linksRequest(query: String, leftProperties: List[String], rightProperties: List[String]): Future[String] = Future {
    write(LinksResponse(query, linkService.linksSearch(query, leftProperties, rightProperties)))
  }
}
