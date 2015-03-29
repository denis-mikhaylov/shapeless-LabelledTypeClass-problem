package app

// To successfully compile

import app.test.A // Comment this
import shapeless.LabelledGeneric

object Main extends App {

//  Uncomment this
//  sealed trait A[+T]
//
//  case class A1[+T](t: T) extends A[T]

  LabelledGeneric[A[String]]
}
