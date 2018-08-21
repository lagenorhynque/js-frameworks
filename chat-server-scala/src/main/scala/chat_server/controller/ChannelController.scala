package chat_server.controller

import chat_server.dao.ChannelsDao
import chat_server.model.Channel

trait ChannelController extends ChannelsDao {
  channelsDao =>

  def listChannels: Seq[Channel] = channelsDao.findAll

  def createChannel: Int = ???

  def fetchChannel(id: Int): Option[Channel] = channelsDao.findById(id)
}
