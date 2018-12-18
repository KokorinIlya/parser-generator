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

  protected def createEof: T

  protected def nameToToken(name: String, content: String): T

  @tailrec
  private def findNextToken(): T = {
    if (!scanner.hasNextLine) {
      createEof
    } else {
      nameToRegex.find { case (_, tokenRegexp) =>
        scanner.hasNext(tokenRegexp)
      } match {
        case Some((nextTokenName, nextTokenRegexp)) =>
          if (!skip.contains(nextTokenName)) {
            val nextText = scanner.next(nextTokenRegexp)
            nameToToken(nextTokenName, nextText)
          } else {
            scanner.next(nextTokenRegexp)
            findNextToken()
          }
        case None => createEof
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

