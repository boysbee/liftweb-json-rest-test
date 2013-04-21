package code
package snippet

import scala.xml.{ NodeSeq, Text }
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.json.JsonDSL._
import code.model._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Printer._

object JsonReply extends RestHelper {
  serve {
    case JsonPost("create_user" :: Nil, JObject(List(
        JField("id", JString(id)),
        JField("nick", JString(nick)),
        JField("avatar_id", JInt(avatarId))
        )) -> _) => {
      var error: String = ""
      var succeed: Boolean = false;

      try {
        val avatarIdAsInt = avatarId.toInt
        //Player.getNewPlayer(id, nick, avatarIdAsInt)
        succeed = true

      } catch {
        case e: Exception => {
          error = e.getStackTraceString
        }
      }

      if (succeed)
        OkResponse()
      else
        JsonResponse(("ans" -> "NOPE") ~ ("error" -> error))
    }
    
  }
}
