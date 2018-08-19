package chat_server.model

import scalikejdbc._

case class Channel(id: Option[Int], name: String)

object Channel extends SQLSyntaxSupport[Channel] {
  override val tableName = "channels"

  def apply(sp: SyntaxProvider[Channel])(rs: WrappedResultSet): Channel = apply(sp.resultName)(rs)
  def apply(rn: ResultName[Channel])(rs: WrappedResultSet): Channel =
    new Channel(id = rs.intOpt(rn.id), name =rs.string(rn.name))
}
