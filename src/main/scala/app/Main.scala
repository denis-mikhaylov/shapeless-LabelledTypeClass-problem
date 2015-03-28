package app

import app.generic.Writes
import app.generic.CoproductContainer.implicits._
import shapeless.LabelledGeneric
import ast2._
object Main extends App {

  val test: SeqDef[StringPair] = StaticSeqDef(Seq(StringPair(DynamicStringDef(Path(List(KeyPathNode("key")))), StaticStringDef("string"))))

  def lgen[A](a: A)(implicit lgen: LabelledGeneric[A]) = lgen.to(a)

  def json[A](a: A)(implicit ev: Writes[A]) = ev.writes(a)

  println(lgen(test))
  println(json(test))
}
