package app.a

sealed trait A[+T]

case class A1[+T](t: T) extends A[T]
