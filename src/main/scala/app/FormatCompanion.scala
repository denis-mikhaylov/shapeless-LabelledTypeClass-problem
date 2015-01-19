package app

import play.api.libs.json._
import shapeless.{:+:, ::, CNil, Coproduct, HList, HNil, Inl, Inr, LabelledTypeClass, LabelledTypeClassCompanion}

object FormatCompanion extends LabelledTypeClassCompanion[Format] {
  object ADTContainer {
    def apply(typeName: String, data: JsValue): JsValue = Json.obj(typeName -> data)
    def unapply(json: JsValue): Option[(String, JsValue)] = json match {
      case JsObject(Seq((typeName, data))) => Some((typeName, data))
      case other => None
    }
  }

  object typeClass extends LabelledTypeClass[Format] {
    def emptyProduct = new Format[HNil] {
      def writes(t: HNil) = Json.obj()
      def reads(json: JsValue): JsResult[HNil] = JsSuccess(HNil)
    }

    def product[F, T <: HList](name : String, FHead : Format[F], FTail : Format[T]) = new Format[F :: T] {
      def writes(ft: F :: T) = {
        val head = FHead.writes(ft.head)
        val tail = FTail.writes(ft.tail)

        Json.obj(name -> head) ++ (tail match {
          case obj: JsObject => obj
          case other => Json.obj()
        })
      }

      def reads(json: JsValue): JsResult[F :: T] = json match {
        case JsObject((`name`, head) +: tail) => for {
          front <- FHead.reads(head)
          back <- FTail.reads(JsObject(tail))
        } yield {
          front :: back
        }
        case _ => JsError("Product miss")
      }
    }

    def emptyCoproduct = new Format[CNil] {
      def writes(t: CNil) = Json.obj()
      def reads(json: JsValue): JsResult[CNil] = JsError()
    }

    def coproduct[L, R <: Coproduct](name: String, CL: => Format[L], CR: => Format[R]) = new Format[L :+: R] {
      def writes(lr: L :+: R): JsValue = lr match {
        case Inl(l) => ADTContainer(name, CL.writes(l))
        case Inr(r) => CR.writes(r)
      }

      def reads(json: JsValue): JsResult[L :+: R] = json match {
        case ADTContainer(`name`, data) => CL.reads(data).map(Inl(_))
        case other => CR.reads(other).map(Inr(_))
      }
    }

    def project[F, G](instance : => Format[G], to : F => G, from : G => F) = new Format[F] {
      def writes(f: F) = instance.writes(to(f))

      def reads(json: JsValue): JsResult[F] = instance.reads(json).map(from)
    }
  }
}

