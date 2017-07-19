package org.kolokolov.plane

import akka.actor.{Actor, ActorLogging, ActorRef}
import org.kolokolov.plane.EventReporter.Register
import org.kolokolov.plane.flightcrew._
import org.kolokolov.plane.FlightSimulator.actorSystem

import scala.concurrent.duration._

/**
  * Created by kolokolov on 6/30/17.
  */
class FlightDispatcher extends Actor with ActorLogging {

  import Altimeter._
  import Pilot._
  implicit val ec = context.dispatcher

  val ticker = context.system.scheduler.schedule(1 second, 1 second, self, Tick)

  val altimeters = actorSystem.actorSelection("/user/*/altimeter")
  val pilots = actorSystem.actorSelection("/user/*/Harry")

  override def preStart() = {
    altimeters ! Register(self)
  }

  def receive = {
    case Tick => pilots ! ReportAltitude
    case PilotReport(report) => {
      log.info("Report: {}", report)
    }
    case AltitudeChanged(altitude) => {
      val currentSender = sender
      val plane = currentSender.path.parent.name
      log.info("Plane {} has changed its altitude to {}",plane,altitude)
    }
  }
}