package app

import play.api.libs.json.Json

class Main extends App {
  println(Json.toJson(B("test")))
}
