package com.imilkaeu.links.database

import akka.event.slf4j.SLF4JLogging
import java.sql._
import com.imilkaeu.links.config.Configuration
import com.imilkaeu.links.domain.{FailureType, Links, Link, Failure}

import scala.Some
import scala.slick.driver.MySQLDriver.simple.Database.threadLocalSession
import scala.slick.driver.MySQLDriver.simple._
import slick.jdbc.meta.MTable

/**
 * Provides DAL for Customer entities for MySQL database.
 */
class LinkDAO extends Configuration with SLF4JLogging {

  private val db = Database.forURL(url = "jdbc:mysql://%s:%d/%s?characterEncoding=utf-8".format(dbHost, dbPort, dbName),
    user = dbUser, password = dbPassword, driver = "com.mysql.jdbc.Driver")

  def linksSearch(qr: String, leftProperties: List[String], rightProperties: List[String]): List[Link] = {
    db.withSession {
      log.debug("Querying: %s".format(qr + "%"))
      val query = for {
        link <- Links
        if link.wordLeft like qr + "%"
        if link.partOfSpeechShortLeft inSet leftProperties
        if link.partOfSpeechShortRight inSet rightProperties
      } yield link

      query.take(100).run.toList
    }
  }

  /**
   * Produce database error description.
   *
   * @param e SQL Exception
   * @return database error description
   */
  protected def databaseError(e: SQLException) =
    Failure("%d: %s".format(e.getErrorCode, e.getMessage), FailureType.DatabaseFailure)

  /**
   * Produce customer not found error description.
   *
   * @param customerId id of the customer
   * @return not found error description
   */
  protected def notFoundError(customerId: Long) =
    Failure("Customer with id=%d does not exist".format(customerId), FailureType.NotFound)
}