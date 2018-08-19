package chat_server.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import chat_server.model.Channel
import spray.json.{DefaultJsonProtocol, JsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val channelFormat: JsonFormat[Channel] = jsonFormat2(Channel.apply)
}
