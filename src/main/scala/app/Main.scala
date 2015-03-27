package app

import java.util.UUID

import app.generic.Writes
import app.generic.CoproductContainer.implicits._
import shapeless.LabelledGeneric

object Main extends App {

  case class Path(path: List[PathNode])

  sealed trait PathNode
  case class KeyPathNode(key: String) extends PathNode
  case class IdxPathNode(idx: Int) extends PathNode

  object Path {
    def \(node: PathNode): Path = Path(List(node))
    object syntax {
      implicit def intToPathNode(idx: Int): PathNode = IdxPathNode(idx)
      implicit def stringToPathNode(key: String): PathNode = KeyPathNode(key)
      implicit class PathOps(o: Path) {
        def \ (node: PathNode): Path = Path(o.path :+ node)
      }
    }
  }

  sealed trait Def

  sealed trait SeqDef[+A ] extends Def

  case class StaticSeqDef[+A ](items: Seq[A]) extends SeqDef[A]

  case class DynamicSeqDef[+A ](path: Path, template: A) extends SeqDef[A]

  sealed trait StringDef extends Def

  case class StaticStringDef(value: String) extends StringDef

  case class DynamicStringDef(path: Path) extends StringDef

  case class ContainerDef(state: Option[StateDef], elements: Option[SeqDef[ElementDef]]) extends Def

  case class StateDef(terms: Option[TermsDef], fixedSum: Option[MoneyDef]) extends Def

  case class TermsDef(id: StringDef) extends Def

  case class MoneyDef(currency: StringDef, amount: StringDef) extends Def

  sealed trait ValueConstraintDef extends Def

  case class ValidatorConstraintDef(validator: ValidatorDef, defaultValue: Option[StringDef]) extends ValueConstraintDef

  case class ChoiceConstraintDef(choices: SeqDef[ChoiceDef], message: LocalizedStringDef, defaultValue: Option[StringDef]) extends ValueConstraintDef

  case class ConstConstraintDef(value: StringDef) extends ValueConstraintDef

  case class SuggestionConstraintDef(source: SuggestionSource, message: LocalizedStringDef) extends ValueConstraintDef

  sealed trait SuggestionSource

  case class P3SuggestionSource(applicationId: P3ApplicationId, suggestionId: String) extends SuggestionSource

  sealed trait ElementDef extends Def

  case class FieldDef(name: StringDef, config: FieldConfigDef) extends ElementDef

  sealed trait FieldConfigDef extends Def

  case class HiddenFieldConfigDef(value: StringDef) extends FieldConfigDef

  sealed trait VisibleFieldConfigDef extends FieldConfigDef

  case class ChoiceFieldConfigDef(constraint: ChoiceConstraintDef, view: ViewDef[ChoiceWidgetDef]) extends VisibleFieldConfigDef

  case class RegularFieldConfigDef(constraint: ValidatorConstraintDef, view: ViewDef[RegularWidgetDef]) extends VisibleFieldConfigDef

  case class ConstFieldConfigDef(constraint: ConstConstraintDef, view: ViewDef[ConstWidgetDef]) extends VisibleFieldConfigDef

  case class SuggestionFieldConfigDef(constraint: SuggestionConstraintDef, view: ViewDef[SuggestionWidgetDef]) extends VisibleFieldConfigDef

  case class ReferenceParameterDef(fieldName: StringDef, triggerReload: Boolean) extends Def

  case class ReferenceDef(title: LocalizedStringDef, message: LocalizedStringDef, parameters: SeqDef[ReferenceParameterDef], resolver: ReferenceResolver) extends ElementDef

  case class ReferenceResolver(id: UUID, source: ContextSource, container: ContainerDef)

  sealed trait ContextSource

  case class P3ContextSource(applicationId: P3ApplicationId, sourceId: String) extends ContextSource

  case class SINAPContextSource(id: String) extends ContextSource

  case class DependencyDef(condition: ConditionDef, container: ContainerDef) extends ElementDef

  case class ChoiceDef(value: StringDef, title: LocalizedStringDef) extends Def

  case class ViewDef[+A <: WidgetDef](title: LocalizedStringDef, prompt: LocalizedStringDef, widget: A, info: Option[LocalizedStringDef]) extends Def

  sealed trait WidgetDef extends Def

  sealed trait RegularWidgetDef extends WidgetDef

  case class TextWidgetDef(mask: Option[StringDef]) extends RegularWidgetDef

  case class ExpiryDateWidgetDef(format: StringDef) extends RegularWidgetDef

  case class DateWidgetDef(format: StringDef) extends RegularWidgetDef

  sealed trait ChoiceWidgetDef extends WidgetDef

  case class ExpansionDef(threshold: Int, title: LocalizedStringDef)

  case class RadioWidgetDef(expansion: Option[ExpansionDef]) extends ChoiceWidgetDef

  case object ListWidgetDef extends ChoiceWidgetDef

  sealed trait ConstWidgetDef extends WidgetDef

  case object InfoWidgetDef extends ConstWidgetDef

  sealed trait SuggestionWidgetDef extends WidgetDef

  case object SuggestionWidgetDef extends SuggestionWidgetDef

  sealed trait ConditionDef extends Def

  case class PredicateConditionDef(field: StringDef, predicate: PredicateDef) extends ConditionDef

  case class ValidityConditionDef(field: StringDef) extends ConditionDef

  case class AndConditionDef(conditions: SeqDef[ConditionDef]) extends ConditionDef

  case class LocalizedStringDef(strings: SeqDef[StringPair]) extends Def

  case class StringPair(lang: StringDef, string: StringDef) extends Def

  sealed trait PredicateDef extends Def

  case class RegexPredicateDef(pattern: StringDef) extends PredicateDef

  case class AndPredicateDef(subpredicates: SeqDef[PredicateDef]) extends PredicateDef

  sealed trait ValidatorDef extends Def

  case class PredicateValidatorDef(predicate: PredicateDef, message: LocalizedStringDef) extends ValidatorDef

  case class P3ApplicationId(id: String, version: Int)


  val test: ContainerDef = Form()


  def lgen[A](a: A)(implicit lgen: LabelledGeneric[A]) = lgen.to(a)

  def json[A](a: A)(implicit ev: Writes[A]) = ev.writes(a)

  println(lgen(test))
  println(json(test))
}
