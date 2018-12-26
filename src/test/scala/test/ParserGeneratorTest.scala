package test

import java.nio.file.{Path, Paths}

import generated._
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

  "ParserGenerator" should "calculate numbers" in {
    val gen = new Generator(
      Paths.get("src/test/resources/Calculator"),
      Paths.get("src/test/scala/generated")
    )
    gen.generate()
    val stream = Files.newInputStream(Paths.get("src/test/resources/CalcExample"))
    val lexer = new CalculatorLexer(stream)
    val parser = new CalculatorParser(lexer)
    val res = parser.parseE()
    println(res)
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
