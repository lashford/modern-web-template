package services

import org.specs2.mutable._

/**
  * Unit tests for the service itself. We would expect that the majority of unit tests would be on components like
  * this.
  */
class SimpleUUIDGeneratorTest extends Specification {

   "SimpleUUIDGenerator" should {

     "generate a UUID" in {
       val uuidGenerator = new SimpleUUIDGenerator

       val uuid = uuidGenerator.generate

       uuid mustNotEqual null
     }
   }
 }