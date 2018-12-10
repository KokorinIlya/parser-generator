package runtime

import java.io.InputStream
import java.util.Scanner
import java.util.regex.Pattern

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

trait AbstractLexer[T <: TextHolder] {
  protected val inputStream: InputStream

  private val scanner = new Scanner(inputStream)

  protected val nameToRegex: List[(String, Pattern)]

  protected val skip: Set[String]

  protected val eofTokenName: String

  protected def nameToToken(name: String): T

  @tailrec
  private def findNextToken(): T = {
    if (!scanner.hasNextLine) {
      nameToToken(eofTokenName)
    } else {
      val (nextTokenName, nextTokenRegexp) = nameToRegex.find { case (_, tokenRegexp) =>
        scanner.hasNext(tokenRegexp)
      }.getOrElse{
        throw new LexingException("Next token not found")
      }
      if (!skip.contains(nextTokenName)) {
        nameToToken(nextTokenName)
      } else {
        findNextToken()
      }
    }
  }

  @throws[LexingException]
  def nextToken(): T = Try {
    findNextToken()
  } match {
    case Failure(exception) => throw new LexingException(exception)
    case Success(value) => value
  }
}

