package app

import app.generic.Writes
import app.generic.CoproductContainer.implicits._
import shapeless.LabelledGeneric

object Main extends App {

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

  val test: SeqDef[StringPair] = StaticSeqDef(Seq(StringPair(DynamicStringDef(Path(List(KeyPathNode("key")))), StaticStringDef("string"))))

  def lgen[A](a: A)(implicit lgen: LabelledGeneric[A]) = lgen.to(a)

  def json[A](a: A)(implicit ev: Writes[A]) = ev.writes(a)

  println(lgen(test))
  println(json(test))
}
