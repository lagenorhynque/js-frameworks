package chat_server.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import chat_server.controller.ChannelController

import scala.concurrent.ExecutionContextExecutor

trait Routes extends JsonSupport with ChannelController {
  channelController: ChannelController =>

  implicit val dispatcher: ExecutionContextExecutor

  val routes = pathPrefix("api") {
    path("channels") {
      get {
        complete(OK, Map("data" -> channelController.listChannels))
      }
    } ~
      pathPrefix("channels" / IntNumber) { channelId =>
        get {
          channelController.fetchChannel(channelId)
            .fold(complete(NotFound, Map("errors" -> Map("channel_id" -> "doesn't exist")))) { channel =>
              complete(OK, Map("data" -> channel))
            }
        }
      }
  }
}
