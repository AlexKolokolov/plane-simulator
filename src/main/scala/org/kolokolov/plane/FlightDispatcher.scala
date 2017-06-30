package org.kolokolov.plane

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * Created by kolokolov on 6/30/17.
  */
class FlightDispatcher extends Actor with ActorLogging {

  import Altimeter._

  def receive = {
    case AltitudeChanged(altitude) => {
      val currentSender = sender
      val plane = currentSender.path.parent.name
      log.info("Plane {} has changed its altitude to {}",plane,altitude)
    }
  }
}

object FlightDispatcher {
  case class RegisterToPlane(plane: ActorRef)
}

