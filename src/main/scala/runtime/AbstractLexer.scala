package runtime

import java.io.InputStream
import java.util.Scanner
import java.util.regex.Pattern

trait AbstractLexer[T <: TextHolder] {
  protected val inputStream: InputStream

  private val scanner = new Scanner(inputStream)

  protected val nameToRegex: List[(String, Pattern)]

  protected val skip: Set[Pattern]

  protected def nameToToken(name: String): T

  def nextToken(): T = {
    ???
  }
}

