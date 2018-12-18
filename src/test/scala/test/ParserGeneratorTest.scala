package test

import java.nio.file.{Files, Path, Paths}

import generated.{ListDescriptionLexer, ListDescriptionParser}
import generators.Generator
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class ParserGeneratorTest extends FlatSpec with BeforeAndAfterAll {
  override def beforeAll(): Unit = {
    Files.walkFileTree(Paths.get("src/test/scala/generated"), new ParserGeneratorTest.TempDirectoryCleaner)

    val gen = new Generator(
      Paths.get("src/test/resources/ListDescription"),
      Paths.get("src/test/scala/generated")
    )
    gen.generate()
  }

  "ParserGenerator" should "parse list descriptions and describe lists" in {
    val stream = Files.newInputStream(Paths.get("src/test/resources/ListExample"))
    val lexer = new ListDescriptionLexer(stream)
    val parser = new ListDescriptionParser(lexer)
    parser.parseLIST()
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
