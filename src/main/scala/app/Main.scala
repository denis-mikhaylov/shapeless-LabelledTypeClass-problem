package app

// To successfully compile
import app.test.A // 1. Comment this line out
import shapeless.LabelledGeneric

object Main extends App {

//  2. Uncomment 3 lines below
//  sealed trait A[+T]
//
//  case class A1[+T](t: T) extends A[T]

  LabelledGeneric[A[String]]
}
