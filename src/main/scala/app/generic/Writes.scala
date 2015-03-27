package app.generic

import java.util.UUID

import play.api.libs.json._
import shapeless._
import shapeless.labelled._

trait Writes[-A] {
  def writes(o: A): JsValue
}

object Writes {
  implicit val stringWrites: Writes[String] = new Writes[String] {
    def writes(o: String) = JsString(o)
  }

  implicit val intWrites: Writes[Int] = new Writes[Int] {
    def writes(o: Int) = JsNumber(o)
  }

  implicit val booleanWrites: Writes[Boolean] = new Writes[Boolean] {
    def writes(o: Boolean) = JsBoolean(o)
  }

  implicit val uuidWrites: Writes[UUID] =  new Writes[UUID] {
    override def writes(o: UUID): JsValue = JsString(o.toString)
  }

  implicit val bigDecimalWrites: Writes[BigDecimal] = new Writes[BigDecimal] {
    def writes(o: BigDecimal) = JsNumber(o)
  }

  implicit def traversableWrites[A](implicit aWrites: Lazy[Writes[A]]): Writes[Traversable[A]] = new Writes[Traversable[A]] {
    def writes(as: Traversable[A]) = JsArray(as.map(aWrites.value.writes).toSeq)
  }

  implicit def listWrites[A](implicit aWrites: Lazy[Writes[A]]): Writes[List[A]] = new Writes[List[A]] {
    def writes(as: List[A]) = JsArray(as.map(aWrites.value.writes).toSeq)
  }

  implicit def OptionWrites[T](implicit fmt: Lazy[Writes[T]]): Writes[Option[T]] = new Writes[Option[T]] {
    def writes(o: Option[T]) =
      o.map(fmt.value.writes).getOrElse(JsNull)
  }

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

  object syntax {
    implicit class WritesOps[T](x: T)(implicit writesA: Writes[T]) {
      def toJson: JsValue = writesA.writes(x)
    }
  }
}
