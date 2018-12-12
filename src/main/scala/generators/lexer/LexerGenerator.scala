package generators.lexer

import generators.tokens.TokensInfo
import input.{Header, SkipTokensHolder}


object LexerGenerator {
  def createLexer(tokensInfo: TokensInfo, skipTokensHolder: SkipTokensHolder, grammarName: String,
                  header: Header): String = {
    val lexerName = s"${grammarName}Lexer"

    val nameToRegex = "\t\t" + tokensInfo.regexps.map { case (name, regexp) =>
      val curTokenName = "\"" + s"$grammarName$name" + "\""
      s"($curTokenName, Pattern.compile($regexp))"
    }.mkString(",\n\t\t")
    val listWithRegexps = s"List(\n$nameToRegex\n\t)"

    val skipTokens = "\t\t" + skipTokensHolder.tokens.map { tokenName =>
      "\"" + s"$grammarName$tokenName" + "\""
    }.mkString(",\n\t\t")
    val setWithSkipTokens = s"Set(\n$skipTokens\n\t)"

    val cases = "\t\t\t" + tokensInfo.regexps.map { case (name, _) =>
      val curTokenName = "\"" + s"$grammarName$name" + "\""
      s"case $curTokenName => $grammarName$name(content)"
    }.mkString("\n\t\t\t")
    val matcher = s"name match {\n$cases\n\t\t}"

    s"""${header.header}
      |
      |import java.io.InputStream
      |import java.util.regex.Pattern
      |
      |class $lexerName(override val inputStream: InputStream) extends runtime.AbstractLexer[${tokensInfo.mainTokenName}] {
      |  override protected def createEof: ${tokensInfo.mainTokenName} = ${grammarName}Eof
      |
      |  override protected val nameToRegex: List[(String, Pattern)] = $listWithRegexps
      |
      |  override protected val skip: Set[String] = $setWithSkipTokens
      |
      |  override protected def nameToToken(name: String, content: String): AbstractInputToken = {
      |    $matcher
      |  }
      |}""".stripMargin
  }
}
