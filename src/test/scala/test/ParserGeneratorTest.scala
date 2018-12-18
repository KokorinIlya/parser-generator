package test

import java.nio.file.{Files, Paths}

import generated.{ListDescriptionLexer, ListDescriptionParser}
import generators.Generator
import org.scalatest.FlatSpec

class ParserGeneratorTest extends FlatSpec {
  "ParserGenerator" should "genarate parser for list descriptions" in {
    /*val gen = new Generator(
      Paths.get("src/test/resources/ListDescription"),
      Paths.get("src/test/scala/generated")
    )
    gen.generate()*/
    val stream = Files.newInputStream(Paths.get("src/test/resources/ListExample"))
    val lexer = new ListDescriptionLexer(stream)
    val parser = new ListDescriptionParser(lexer)
    parser.parseLIST()
  }
}
