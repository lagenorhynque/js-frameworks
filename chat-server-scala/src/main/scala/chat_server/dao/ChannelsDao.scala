package chat_server.dao

import chat_server.model.Channel
import scalikejdbc._

trait ChannelsDao {
  val c = Channel.syntax("c")

  def findAll: Seq[Channel] =
    DB readOnly { implicit session =>
      withSQL {
        select
          .from(Channel as c)
          .orderBy(c.name.asc, c.id.asc)
      }.map(Channel(c)).list.apply()
    }
}
