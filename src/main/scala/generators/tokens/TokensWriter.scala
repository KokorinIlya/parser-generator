package generators.tokens

import java.nio.file.{Files, Path, StandardOpenOption}

import input.Header
import utils.IOUtils

object TokensWriter {
  def writeTokens(pathToFileWithTokens: Path, tokensInfo: TokensInfo, lexerHeader: Header){
    IOUtils.using(Files.newBufferedWriter(pathToFileWithTokens, StandardOpenOption.CREATE)) { writer =>
      writer.write(lexerHeader.header)
      writer.newLine()
      writer.write(tokensInfo.mainTokenDescription)
      writer.newLine()
      writer.write(tokensInfo.tokensDescription.mkString("\n"))
      writer.close()
    }
  }
}
