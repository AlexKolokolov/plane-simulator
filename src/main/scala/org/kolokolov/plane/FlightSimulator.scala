package org.kolokolov.plane

import akka.actor.{ActorSystem, Props}
import scala.concurrent.duration._
import org.kolokolov.plane.flightcrew._

/**
  * Created by kolokolov on 6/30/17.
  */
object FlightSimulator extends App {

  val actorSystem = ActorSystem("PlaneSimulation")
  implicit val ec = actorSystem.dispatcher

  import Plane._
  import Pilot._

  val plane1 = actorSystem.actorOf(Props[Plane],"A380")
  val flightDispatcher = actorSystem.actorOf(Props[FlightDispatcher],"dispatcher")
  val pilot1 = actorSystem.actorSelection(plane1.path.child("Harry"))

  actorSystem.scheduler.scheduleOnce(2 seconds) {
    plane1 ! StickBack(0.12f)
  }

  actorSystem.scheduler.scheduleOnce(3 seconds) {
    pilot1 ! Drink
//    plane1 ! Missile
  }

  actorSystem.scheduler.scheduleOnce(4 seconds) {
    plane1 ! StickForward(0.12f)
  }

  actorSystem.scheduler.scheduleOnce(5 seconds) {
    actorSystem.actorSelection(plane1.path.child("John")) ! TakeControls
  }

  actorSystem.scheduler.scheduleOnce(6 seconds) {
    plane1 ! StickForward(0.23f)
  }

  actorSystem.scheduler.scheduleOnce(7 seconds) {
    actorSystem.terminate()
  }
}