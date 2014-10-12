package com.imilkaeu.links.domain

import scala.slick.driver.MySQLDriver.simple._

case class Link(wordLeft: Word, wordRight: Word, count: Int)
case class Word(word: String, partOfSpeechLong: String, partOfSpeechShort: String)

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

  def * = wordLeft ~ partOfSpeechLongLeft ~ partOfSpeechShortLeft ~ wordRight ~ partOfSpeechLongRight ~ partOfSpeechShortRight ~ count <> (mapRow _, unMapRow _)

  private def mapRow(wordLeft: String, partOfSpeechLongLeft: String, partOfSpeechShortLeft: String,
                      wordRight: String, partOfSpeechLongRight: String, partOfSpeechShortRight: String,
                      count: Int): Link = {
    val wl = Word(wordLeft, partOfSpeechLongLeft, partOfSpeechShortLeft)
    val wr = Word(wordRight, partOfSpeechLongRight, partOfSpeechShortRight)
    Link(wl, wr, count)
  }

  private def unMapRow(link: Link) = {
    val wordLeft = link.wordLeft.word
    val partOfSpeechLongLeft = link.wordLeft.partOfSpeechLong
    val partOfSpeechShortLeft = link.wordLeft.partOfSpeechShort
    val wordRight = link.wordRight.word
    val partOfSpeechLongRight = link.wordRight.partOfSpeechLong
    val partOfSpeechShortRight = link.wordRight.partOfSpeechShort

    val tuple = (wordLeft, partOfSpeechLongLeft, partOfSpeechShortLeft, wordRight, partOfSpeechLongRight, partOfSpeechShortRight, link.count)
    Some(tuple)
  }
}