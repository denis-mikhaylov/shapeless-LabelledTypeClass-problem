package app.test

sealed trait Def

sealed trait SeqDef[+A <: Def] extends Def

case class StaticSeqDef[+A <: Def](items: Seq[A]) extends SeqDef[A]

case class DynamicSeqDef[+A <: Def](path: Path, template: A) extends SeqDef[A]

sealed trait StringDef extends Def

case class StaticStringDef(value: String) extends StringDef

case class DynamicStringDef(path: Path) extends StringDef

case class Path(path: List[PathNode])

sealed trait PathNode
case class KeyPathNode(key: String) extends PathNode
case class IdxPathNode(idx: Int) extends PathNode
