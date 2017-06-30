package org.kolokolov.plane

import akka.actor.Actor
import scala.concurrent.duration._

/**
  * Created by kolokolov on 6/30/17.
  */
class Altimeter extends Actor with EventReporter {

  import Altimeter._
  implicit val ec = context.system.dispatcher

  private var altitude = 1000f

  private var climbRate = 0f

  private var lastTickTime = System.currentTimeMillis

  private val ticker = context.system.scheduler.schedule(100 millis, 100 millis, self, Tick)

  private def updateAltitude(): Unit = {
    val lastAltitude = altitude
    val tickTime = System.currentTimeMillis
    altitude += (tickTime - lastTickTime) * climbRate
    lastTickTime = tickTime
    if (altitude != lastAltitude) sendEvent(AltitudeChanged(altitude))
  }

  def tuneAltitude: Receive = {
    case Tick => updateAltitude()
    case ClimbRateChanged(newClimbRate) => climbRate = newClimbRate
  }

  def receive = tuneAltitude orElse manageListeners
}

object Altimeter {
  case object Tick
  case class ClimbRateChanged(newClimbRate: Float)
  case class AltitudeChanged(newAltitude: Float)
}
