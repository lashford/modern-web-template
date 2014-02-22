package controllers

import org.specs2.mock.Mockito
import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import services.UUIDGenerator
import java.util.UUID

/**
 * We focus here on testing the controller only - not the infrastructure in front or behind it. Using dependency
 * injection allows the application controller to become testable. It is conceivable that you might have a unit
 * test for the controller if there is enough logic contained in the method that makes it worth testing - the
 * integration test might offer a more useful test if there is not given that you are then testing that the
 * route is configured properly.
 */
class ApplicationUnitTest extends Specification with Mockito {
  
  "Application" should {
    
    "invoke the UUID generator" in {
      val uuidGenerator = mock[UUIDGenerator]
      val application = new controllers.Application(uuidGenerator)

      uuidGenerator.generate returns UUID.randomUUID()

      application.randomUUID(FakeRequest())

      there was one(uuidGenerator).generate
    }
  }
}