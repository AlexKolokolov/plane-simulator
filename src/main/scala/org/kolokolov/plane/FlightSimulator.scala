package org.kolokolov.plane

import akka.actor.{ActorSystem, Props}
import scala.concurrent.duration._

/**
  * Created by kolokolov on 6/30/17.
  */
object FlightSimulator extends App {

  val actorSystem = ActorSystem("system")
  implicit val ec = actorSystem.dispatcher

  import Plane._
  import EventReporter._

  val plane1 = actorSystem.actorOf(Props[Plane],"plane1")
  val plane2 = actorSystem.actorOf(Props[Plane],"plane2")
  val flightDispatcher = actorSystem.actorOf(Props[FlightDispatcher],"dispatcher")
  val altimeters = actorSystem.actorSelection("/user/*/altimeter")
  altimeters ! Register(flightDispatcher)

  actorSystem.scheduler.scheduleOnce(2 seconds) {
    plane1 ! StickBack(0.12f)
  }

  actorSystem.scheduler.scheduleOnce(3 seconds) {
    plane2 ! StickBack(0.15f)
  }

  actorSystem.scheduler.scheduleOnce(4 seconds) {
    plane1 ! StickForward(0.12f)
  }

  actorSystem.scheduler.scheduleOnce(5 seconds) {
    plane2 ! StickForward(0.2f)
  }

  actorSystem.scheduler.scheduleOnce(6 seconds) {
    plane1 ! StickForward(0.23f)
  }

  actorSystem.scheduler.scheduleOnce(7 seconds) {
    actorSystem.terminate()
  }
}
