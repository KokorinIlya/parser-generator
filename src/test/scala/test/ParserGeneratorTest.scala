package test

import java.nio.file.{Files, Path, Paths}

import generated.{ListDescriptionLexer, ListDescriptionParser, ListLengthLexer, ListLengthParser}
import generators.Generator
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class ParserGeneratorTest extends FlatSpec with BeforeAndAfterAll {
  override def beforeAll(): Unit = {
    Files.createDirectories(Paths.get("src/test/scala/generated"))
    Files.walkFileTree(Paths.get("src/test/scala/generated"), new ParserGeneratorTest.TempDirectoryCleaner)
  }

  "ParserGenerator" should "parse list descriptions and describe lists" in {
    val gen = new Generator(
      Paths.get("src/test/resources/ListDescription"),
      Paths.get("src/test/scala/generated")
    )
    gen.generate()
    val stream = Files.newInputStream(Paths.get("src/test/resources/ListExample"))
    val lexer = new ListDescriptionLexer(stream)
    val parser = new ListDescriptionParser(lexer)
    parser.parseLIST()
  }

  it should "parse and get list length" in {
    val gen = new Generator(
      Paths.get("src/test/resources/ListLength"),
      Paths.get("src/test/scala/generated")
    )
    gen.generate()
    val stream = Files.newInputStream(Paths.get("src/test/resources/ListExample"))
    val lexer = new ListLengthLexer(stream)
    val parser = new ListLengthParser(lexer)
    val res = parser.parseLIST("Hello")
    assert(res == 4)
  }
}

object ParserGeneratorTest {
  class TempDirectoryCleaner extends SimpleFileVisitor[Path] {
    override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
      Files.delete(file)
      FileVisitResult.CONTINUE
    }

    override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
      Files.delete(dir)
      FileVisitResult.CONTINUE
    }
  }
}
