package com.imilkaeu.links.boot

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.imilkaeu.links.config.Configuration
import com.imilkaeu.links.rest.RestServiceActor
import spray.can.Http

object Boot extends App with Configuration {

  // create an actor system for application
  implicit val system = ActorSystem("rest-service-example")

  // create and start rest service actor
  val restService = system.actorOf(Props[RestServiceActor], "rest-endpoint")

  // start HTTP server with rest service actor as a handler
  IO(Http) ! Http.Bind(restService, serviceHost, servicePort)
}