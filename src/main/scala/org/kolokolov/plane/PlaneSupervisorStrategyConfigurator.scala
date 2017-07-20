package org.kolokolov.plane

import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{OneForOneStrategy, SupervisorStrategy, SupervisorStrategyConfigurator}

class PlaneSupervisorStrategyConfigurator extends SupervisorStrategyConfigurator {
  override def create(): SupervisorStrategy = {
    OneForOneStrategy() {
      case _ => Restart
    }
  }
}
