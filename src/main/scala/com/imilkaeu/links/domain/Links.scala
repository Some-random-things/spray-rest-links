package com.imilkaeu.links.domain

import scala.slick.driver.MySQLDriver.simple._

case class Link(wordLeft: String, partOfSpeechLongLeft: String, partOfSpeechShortLeft: String,
                wordRight: String, partOfSpeechLongRight: String, partOfSpeechShortRight: String,
                count: Int)

/**
 * Mapped customers table object.
 */
object Links extends Table[Link]("linksview") {

  def wordLeft = column[String]("word1")
  def partOfSpeechLongLeft = column[String]("pos1")
  def partOfSpeechShortLeft = column[String]("sh1")
  def wordRight = column[String]("word2")
  def partOfSpeechLongRight = column[String]("pos2")
  def partOfSpeechShortRight = column[String]("sh2")
  def count = column[Int]("count")

  def * = wordLeft ~ partOfSpeechLongLeft ~ partOfSpeechShortLeft ~ wordRight ~ partOfSpeechLongRight ~ partOfSpeechShortRight ~ count <> (Link, Link.unapply _)
}