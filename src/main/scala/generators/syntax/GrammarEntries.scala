package generators.syntax

sealed trait Entry {
  val name: String
}

case class NonTerminal(override val name: String) extends Entry

sealed trait FirstEntry extends Entry

sealed trait FollowEntry extends Entry

object Epsilon extends  FirstEntry {
  override val name: String = "eps"
}

case class Terminal(override val name: String) extends FirstEntry with FollowEntry

case object Dollar extends FollowEntry {
  override val name: String = "$"
}
