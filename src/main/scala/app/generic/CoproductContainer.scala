package app.generic

import play.api.libs.json.JsValue

@scala.annotation.implicitNotFound("Can not find CoproductContainer in scope")
trait CoproductContainer {
  def apply(typeName: String, data: JsValue): JsValue
  def unapply(json: JsValue): Option[(String, JsValue)]
}

object CoproductContainer {
  def default: CoproductContainer = new CoproductContainer {
    import play.api.libs.json.{JsObject, Json, __}

    private val typeKey = "type"

    override def unapply(json: JsValue): Option[(String, JsValue)] =
      json.asOpt((__ \ typeKey).read[String]).map((_, json))

    override def apply(typeName: String, data: JsValue): JsValue =
      Json.obj(typeKey -> typeName) ++ (data match {
        case o: JsObject => o
        case _ => Json.obj()
      })
  }
  object implicits {
    implicit val default: CoproductContainer = CoproductContainer.default
  }
}
