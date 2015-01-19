package app

sealed trait Super
case class A(int: Int) extends Super
case class B(string: String) extends Super
case class C(a: A, b: B) extends Super

object Super {
  implicit val format = FormatCompanion[Super]
}
