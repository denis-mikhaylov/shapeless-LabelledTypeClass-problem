package app

import java.util.UUID

import Main._
import Path.syntax._

object Form {
  def apply(): ContainerDef = ContainerDef(
    state = Some(StateDef(
      terms = Some(TermsDef(StaticStringDef("1973"))),
      fixedSum = None
    )),
    elements = Some(StaticSeqDef(Seq(
      FieldDef(
        name = StaticStringDef("driving_license"),
        config = RegularFieldConfigDef(
          constraint = ValidatorConstraintDef(
            validator = PredicateValidatorDef(
              predicate = RegexPredicateDef(StaticStringDef("^\\d{2} .{2} \\d{6}$")),
              message = LocalizedStringDef(StaticSeqDef(Seq(
                StringPair(StaticStringDef("ru"), StaticStringDef("Неверный формат номера ВУ"))
              )))
            ),
            defaultValue = None
          ),
          view = ViewDef(
            title = LocalizedStringDef(StaticSeqDef(Seq(
              StringPair(StaticStringDef("ru"), StaticStringDef("Номер ВУ"))
            ))),
            prompt = LocalizedStringDef(StaticSeqDef(Seq(
              StringPair(StaticStringDef("ru"), StaticStringDef("Номер ВУ"))
            ))),
            widget = TextWidgetDef(Some(StaticStringDef("dd ** dddddd"))),
            info = None
          )
        )
      ),
      FieldDef(
        name = StaticStringDef("vehicle_registration_cert"),
        config = RegularFieldConfigDef(
          constraint = ValidatorConstraintDef(
            validator = PredicateValidatorDef(
              predicate = RegexPredicateDef(StaticStringDef("^\\d{2} .{2} \\d{6}$")),
              message = LocalizedStringDef(StaticSeqDef(Seq(
                StringPair(StaticStringDef("ru"), StaticStringDef("Неверный формат СТС"))
              )))
            ),
            defaultValue = None
          ),
          view = ViewDef(
            title = LocalizedStringDef(StaticSeqDef(Seq(
              StringPair(StaticStringDef("ru"), StaticStringDef("Номер СТС"))
            ))),
            prompt = LocalizedStringDef(StaticSeqDef(Seq(
              StringPair(StaticStringDef("ru"), StaticStringDef("Номер СТС"))
            ))),
            widget = TextWidgetDef(Some(StaticStringDef("dd ** dddddd"))),
            info = None
          )
        )
      ),
      ReferenceDef(
        title = LocalizedStringDef(StaticSeqDef(Seq(
          StringPair(StaticStringDef("ru"), StaticStringDef("Загрузить штрафы"))
        ))),
        message = LocalizedStringDef(StaticSeqDef(Seq(
          StringPair(StaticStringDef("ru"), StaticStringDef("Загрузите штрафы для оплаты"))
        ))),
        parameters = StaticSeqDef(Seq(
          ReferenceParameterDef(StaticStringDef("driving_license"), triggerReload = true),
          ReferenceParameterDef(StaticStringDef("vehicle_registration_cert"), triggerReload = true)
        )),
        resolver = ReferenceResolver(
          id = UUID.fromString("d16b3a3f-6edb-430c-9fa5-1d81231e93c5"),
          source = SINAPContextSource("gibddFines"),
          container = ContainerDef(
            state = None,
            elements = Some(StaticSeqDef(Seq(
              FieldDef(
                name = StaticStringDef("account"),
                config = ChoiceFieldConfigDef(
                  constraint = ChoiceConstraintDef(
                    choices = DynamicSeqDef(
                      path = Path \ "fines",
                      template = ChoiceDef(
                        title = LocalizedStringDef(StaticSeqDef(Seq(
                          StringPair(StaticStringDef("ru"), DynamicStringDef(Path \ "title")))
                        )),
                        value = DynamicStringDef(Path \ "id")
                      )
                    ),
                    message = LocalizedStringDef(StaticSeqDef(Seq(
                      StringPair(StaticStringDef("ru"), StaticStringDef("Выберите постановление для оплаты"))
                    ))),
                    defaultValue = None
                  ),
                  view = ViewDef(
                    title = LocalizedStringDef(StaticSeqDef(Seq(
                      StringPair(StaticStringDef("ru"), StaticStringDef("Ваши постановления"))
                    ))),
                    prompt = LocalizedStringDef(StaticSeqDef(Seq(
                      StringPair(StaticStringDef("ru"), StaticStringDef("Выберите постановление"))
                    ))),
                    widget = RadioWidgetDef(Some(ExpansionDef(
                      threshold = 4,
                      title = LocalizedStringDef(StaticSeqDef(Seq(
                        StringPair(StaticStringDef("ru"), StaticStringDef("Показать все постановления"))
                      )))
                    ))),
                    info = None
                  )
                )
              ),
              DependencyDef(
                condition = ValidityConditionDef(StaticStringDef("account")),
                container = ContainerDef(
                  state = None,
                  elements = Some(DynamicSeqDef(
                    path = Path \ "fines",
                    template = DependencyDef(
                      condition = PredicateConditionDef(
                        field = StaticStringDef("account"),
                        predicate = RegexPredicateDef(
                          pattern = DynamicStringDef(Path \ "id")
                        )
                      ),
                      container = ContainerDef(
                        state = Some(StateDef(
                          terms = Some(TermsDef(StaticStringDef("1973"))),
                          fixedSum = Some(MoneyDef(
                            currency = StaticStringDef("643"),
                            amount = DynamicStringDef(Path \ "amount")
                          ))
                        )),
                        elements = Some(DynamicSeqDef(
                          path = Path \ "extras",
                          template = FieldDef(
                            name = DynamicStringDef(Path \ "name"),
                            config = HiddenFieldConfigDef(DynamicStringDef(Path \ "value"))
                          )
                        ))
                      )
                    )
                  ))
                )
              ),
              DependencyDef(
                condition = ValidityConditionDef(StaticStringDef("account")),
                container = ContainerDef(
                  state = None,
                  elements = Some(StaticSeqDef(Seq(
                    FieldDef(
                      name = StaticStringDef("from_name_f"),
                      config = RegularFieldConfigDef(
                        constraint = ValidatorConstraintDef(
                          validator = PredicateValidatorDef(
                            predicate = RegexPredicateDef(StaticStringDef("^.+$")),
                            message = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Поле не может быть пустым")))))
                          ),
                          defaultValue = None
                        ),
                        view = ViewDef(
                          title = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Фамилия"))))),
                          prompt = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Введите фамилию"))))),
                          widget = TextWidgetDef(None),
                          info = None
                        )
                      )
                    ),
                    FieldDef(
                      name = StaticStringDef("from_name"),
                      config = RegularFieldConfigDef(
                        constraint = ValidatorConstraintDef(
                          validator = PredicateValidatorDef(
                            predicate = RegexPredicateDef(StaticStringDef("^.+$")),
                            message = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Поле не может быть пустым")))))
                          ),
                          defaultValue = None
                        ),
                        view = ViewDef(
                          title = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Имя"))))),
                          prompt = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Введите имя"))))),
                          widget = TextWidgetDef(None),
                          info = None
                        )
                      )
                    ),
                    FieldDef(
                      name = StaticStringDef("from_name_p"),
                      config = RegularFieldConfigDef(
                        constraint = ValidatorConstraintDef(
                          validator = PredicateValidatorDef(
                            predicate = RegexPredicateDef(StaticStringDef("^.+$")),
                            message = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Поле не может быть пустым")))))
                          ),
                          defaultValue = None
                        ),
                        view = ViewDef(
                          title = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Отчество"))))),
                          prompt = LocalizedStringDef(StaticSeqDef(Seq(StringPair(StaticStringDef("ru"), StaticStringDef("Введите отчество"))))),
                          widget = TextWidgetDef(None),
                          info = None
                        )
                      )
                    )
                  )))
                )
              )
            )))
          )
        )
      )
    )))
  )
}
