package org.kolokolov.plane

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest._

/**
  * Created by kolokolov on 7/1/17.
  */
class EventReporterTest extends TestKit(ActorSystem("EventReporterSpec"))
                        with WordSpecLike
                        with MustMatchers
                        with BeforeAndAfterAll  {
  import EventReporter._

  override def afterAll(): Unit = system.terminate()

  "EventReporter" should {
    "allow us to register a listener" in {
      val reporter = TestActorRef[TestEventReporter].underlyingActor
      reporter.receive(Register(testActor))
      reporter.listeners must contain (testActor)
    }
    "send message to test actor" in {
      val reporter = TestActorRef[TestEventReporter]
      reporter ! Register(testActor)
      reporter.underlyingActor.sendEvent("testEvent")
      expectMsg("testEvent")
    }
  }
}

class TestEventReporter extends Actor with ProductionEventReporter {
  def receive = manageListeners
}