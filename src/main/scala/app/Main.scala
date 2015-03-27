package app

import app.ast.Def
import play.api.libs.json.{Writes, Json}

object   Main extends App {
  import generic.CoproductContainer.implicits._
  import generic.GenericWrites._
  import Writes._ // Used to introduce predefined Writes instances into implicit scope

  val definition = Form()

  val result = Json.toJson(definition)

  println(result)
}
