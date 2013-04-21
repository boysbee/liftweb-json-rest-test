package code
package snippet

import net.liftweb._
import http._
import net.liftweb.util._
import net.liftweb.common._
import Helpers._
import lib._
import net.liftweb.mocks.MockHttpServletRequest
import net.liftweb.json.JsonDSL._
import net.liftweb.mockweb.MockWeb._
import net.liftweb.mockweb._
import bootstrap.liftweb.Boot
import net.liftweb.json.JsonAST.JObject
import org.specs2.Specification

object JsonReplyTest extends Specification {

  private val liftRules = new LiftRules()

  LiftRulesMocker.devTestLiftRulesInstance.doWith(liftRules) {
    new Boot().boot
  }

  val testUrl = "http://localhost:8080"

  val mockReq =
    new MockHttpServletRequest(testUrl, "/create_user")

  mockReq.method = "POST"

  mockReq.body = ("id" -> "ziomo") ~ ("nick" -> "ziomisław") ~ ("avatar_id" -> 2)

  /* I need a test that connects to JsonReply, posts this body, and then I want to be able to
   * query the database for - say - update in Player model (not present here)
   */

}
