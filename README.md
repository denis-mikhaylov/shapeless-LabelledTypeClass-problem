# shapeless-problem
Investigation of generic type class derivation

Examples should be run using `sbt clean run`.

`ast1-success` - My full AST is defined inside `Main` class. 
`LabelledGeneric[ContainerDef]` - ok
`Writes[ContainerDef]` - ok

`ast1-fail` - I moved definition of AST to package `ast`.
`LabelledGeneric[ContainerDef]` - ok
`Writes[ContainerDef]` - fails

`ast2-success` - Small part of AST containing polymorphic types is defined inside `Main` class.
`LabelledGeneric[SeqDef[StringPair]]` - ok
`Writes[SeqDef[StringPair]]` - ok

`ast2-fail` - I moved AST from `ast2-success` from `Main` to its own package `ast2`.
`LabelledGeneric[SeqDef[StringPair]]` - fails
`Writes[SeqDef[StringPair]]` - fails
