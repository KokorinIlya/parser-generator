package utils

object IOUtils {
  def using[T <: AutoCloseable, R](resource: => T) (action: T => R): R = {
    val usedResource = resource

    try {
      val result = action(usedResource)
      resource.close()
      result
    } catch {
      case e: Throwable =>
        try {
          resource.close()
        } catch {
          case ee: Throwable => e.addSuppressed(ee)
        }
        throw e
    }
  }
}
