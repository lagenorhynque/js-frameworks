package chat_server.http

import akka.http.scaladsl.server.Directives.{complete, get, path, pathPrefix}
import chat_server.dao.ChannelsDao

import scala.concurrent.ExecutionContextExecutor

trait Routes extends JsonSupport with ChannelsDao {
  channelsDao: ChannelsDao =>

  implicit val dispatcher: ExecutionContextExecutor

  val routes = pathPrefix("api") {
    path("channels") {
      get {
        val channels = channelsDao.findAll
        complete(Map("data" -> channels))
      }
    }
  }
}
