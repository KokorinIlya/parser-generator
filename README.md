# parser-generator
Simple parser and lexer generator for LL(1) grammars

**Table of contents**

<!--- TOC -->

 * [Lexer and parser generation](#lexer-and-parser-generation)
 * [Grammar description](#grammar-description)

<!--- END_TOC -->

## Lexer and parser generation

To create lexer and parser, run the following code.

```scala
val gen = new Generator(
  pathToGrammar,
  pathToDirectoryForCode
)
gen.generate()
```

Where ```pathToGrammar``` is a path to file with grammar description (see [Grammar description](#grammar-description) section for details), and ```pathToDirectoryForCode``` is a path do directory, where generated code (lexer, parser and tokens description) will be placed.

## Grammar description

TODO
