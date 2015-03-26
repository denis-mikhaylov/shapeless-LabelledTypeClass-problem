package app.generic

import play.api.libs.json._
import shapeless._
import shapeless.labelled._

object GenericWrites {
  implicit def writesGeneric[A, R](implicit gen: LabelledGeneric.Aux[A, R], wRepr: Lazy[Writes[R]]): Writes[A] =
    new Writes[A] {
      override def writes(o: A): JsValue = wRepr.value.writes(gen.to(o))
    }

  implicit val writesHNil: Writes[HNil] = new Writes[HNil] {
    override def writes(o: HNil): JsValue = Json.obj()
  }

  implicit def writesHCons[K <: Symbol, V, T <: HList]
  (implicit
   K: Witness.Aux[K],
   V: Lazy[Writes[V]],
   T: Lazy[Writes[T]]
    ): Writes[FieldType[K, V] :: T] =
    new Writes[::[FieldType[K, V], T]] {
      override def writes(o: ::[FieldType[K, V], T]): JsValue = {
        val head = V.value.writes(o.head) match {
          case JsNull => Json.obj()
          case v => Json.obj(K.value.name -> v)
        }

        val tail = T.value.writes(o.tail) match {
          case o: JsObject => o
          case _ => Json.obj()
        }

        head ++ tail
      }
    }

  implicit def writesCNil: Writes[CNil] =
    new Writes[CNil] {
      override def writes(o: CNil): JsValue = Json.obj()
    }

  implicit def writesCCons[K <: Symbol, V, T <: Coproduct]
  (implicit
   K: Witness.Aux[K],
   V: Lazy[Writes[V]],
   T: Lazy[Writes[T]],
   cpc: CoproductContainer
    ): Writes[FieldType[K, V] :+: T] =
    new Writes[FieldType[K, V] :+: T] {
      override def writes(o: :+:[FieldType[K, V], T]): JsValue = o match {
        case Inl(l) => cpc(K.value.name, V.value.writes(l))
        case Inr(r) => T.value.writes(r)
      }
    }
}
