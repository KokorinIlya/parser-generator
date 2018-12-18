package generators.writers

import java.nio.file.{Files, Path, StandardOpenOption}

import utils.IOUtils

object CodeWriter {
  def writeCode(pathToFileWithCode: Path, code: String) {
    IOUtils.using(Files.newBufferedWriter(pathToFileWithCode, StandardOpenOption.CREATE)) { writer =>
      writer.write(code)
      writer.newLine()
      writer.close()
    }
  }
}
