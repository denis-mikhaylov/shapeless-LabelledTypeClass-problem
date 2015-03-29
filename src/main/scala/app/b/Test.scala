package app.b

import app.a.A
import shapeless.LabelledGeneric

object Test {
  def lgen = LabelledGeneric[A[String]]
}
