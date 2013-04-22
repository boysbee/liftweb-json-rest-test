package code.api

import net.liftweb.http._
import net.liftweb.util.Helpers._
import net.liftweb.mocks.MockHttpServletRequest
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import bootstrap.liftweb.Boot
import net.liftweb.common.Full
import code.snippet.WebSpec2

object WebSpecSpecBoot {
  def boot() {
    println("WebSpecSpec Booting up")

    LiftRules.addToPackages("code")

    println("WebSpecSpec Boot complete")
  }
}

class JsonReplyTest extends WebSpec2(WebSpecSpecBoot.boot _) {

  val testUrl = "http://localhost:8080"
  val mockReq = new MockHttpServletRequest(testUrl)

  mockReq.method = "POST"
  mockReq.path = "/create_user"
  mockReq.body = ("id" -> "ziomo") ~ ("nick" -> "ziomisław") ~ ("avatar_id" -> 2)

  "JsonReplyTest" should {
    args(sequential=true)

      "Save one player" withReqFor( mockReq ) in { req =>
      println("req is %s " format req)
      (JsonReply(req)() match {
        case Full(OkResponse()) => true
        case other => failure("Invalid response : " + other); false
      }) must_== true

    }
  }

}
