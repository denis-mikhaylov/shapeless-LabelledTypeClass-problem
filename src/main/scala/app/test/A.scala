package app.test

sealed trait A[+T]

case class A1[+T](t: T) extends A[T]
