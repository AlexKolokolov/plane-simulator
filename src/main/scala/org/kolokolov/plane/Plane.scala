package org.kolokolov.plane

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.Timeout
import org.kolokolov.plane.EventReporter.Register
import org.kolokolov.plane.flightcrew.{Pilot,CoPilot}

import scala.concurrent.duration._

/**
  * Created by kolokolov on 6/30/17.
  */
class Plane extends Actor with ActorLogging {

  import Plane._
  import Altimeter._
  implicit val timeout = Timeout(5 seconds)

  val config = context.system.settings.config

  private var climbRate = 0f

  private val altimeter = context.actorOf(Props(Altimeter()), "altimeter")

  override def preStart(): Unit = {
    altimeter ! Register(self)
    val pilotName = config.getString("org.kolokolov.plane.flightcrew.pilotName")
    val copilotName = config.getString("org.kolokolov.plane.flightcrew.copilotName")
    context.actorOf(Props[Pilot], pilotName)
    context.actorOf(Props[CoPilot], copilotName)
  }

  def changeClimbRate(value: Float): Unit = {
    climbRate += value
    log.info("Climb rate has changed to {}", climbRate)
  }

  override def receive = {
    case StickForward(amount) => {
      changeClimbRate(-amount)
      altimeter ! ClimbRateChanged(climbRate)
    }
    case StickBack(amount) => {
      changeClimbRate(amount)
      altimeter ! ClimbRateChanged(climbRate)
    }
    case AltitudeChanged(newAltitude) => log.info("Current altitude: {}", newAltitude)
  }
}

object Plane {
  case class StickForward(amount: Float)
  case class StickBack(amount: Float)
}