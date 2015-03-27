package app

import app.ast.ContainerDef
import app.generic.Writes
import app.generic.CoproductContainer.implicits._
import shapeless.LabelledGeneric

object Main extends App {

  val test: ContainerDef = Form()


  def lgen[A](a: A)(implicit lgen: LabelledGeneric[A]) = lgen.to(a)

  def json[A](a: A)(implicit ev: Writes[A]) = ev.writes(a)

  println(lgen(test))
  println(json(test))
}
