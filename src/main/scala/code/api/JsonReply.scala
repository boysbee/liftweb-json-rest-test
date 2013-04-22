package code.api

import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.util.Helpers._


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
