package org.kolokolov.plane.flightcrew

import akka.actor.Actor
import org.kolokolov.plane._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Pilot {
  case object ReportAltitude
  case class PilotReport(report: String)
  case object TakeControls
  case object Drink
}

class Pilot extends Actor {

  import Pilot._
  import Altimeter._

  implicit val ec = context.dispatcher
  implicit val timeout = Timeout(1 second)

  val altimeter = context.actorSelection("../altimeter")

  override def postRestart(reason: Throwable): Unit = {
    val dispatcher = context.actorSelection("/user/dispatcher")
    val report = s"I have resurrected after the death cosed by: ${reason.getMessage}"
    dispatcher ! PilotReport(report)
  }

  def receive: Receive = {
    case ReportAltitude =>
      val currentSender = sender
      val pilotName = self.path.name
      val planeName = self.path.parent.name
      (altimeter ? CurrentAltitudeRequest).foreach {
      case CurrentAltitude(altitude) => {
        val report = s"I am $pilotName from $planeName. My altitude is $altitude"
        currentSender ! PilotReport(report)
      }
    }
    case Drink => throw new RuntimeException("I am dead drunk!")
  }
}

class CoPilot extends Actor {
  import Pilot._
  def receive: Receive = {
    case TakeControls => {
      val coPilotName = self.path.name
      val planeName = self.path.parent.name
      val dispatcher = context.actorSelection("/user/dispatcher")
      val report = s"I am $coPilotName. I took control over the $planeName"
      dispatcher ! PilotReport(report)
    }
  }
}

