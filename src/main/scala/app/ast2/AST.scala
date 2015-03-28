package app.ast2

case class Path(path: List[PathNode])

sealed trait PathNode
case class KeyPathNode(key: String) extends PathNode
case class IdxPathNode(idx: Int) extends PathNode

sealed trait Def

sealed trait SeqDef[+A ] extends Def

case class StaticSeqDef[+A ](items: Seq[A]) extends SeqDef[A]

case class DynamicSeqDef[+A ](path: Path, template: A) extends SeqDef[A]

sealed trait StringDef extends Def

case class StaticStringDef(value: String) extends StringDef

case class DynamicStringDef(path: Path) extends StringDef

case class StringPair(lang: StringDef, string: StringDef) extends Def
