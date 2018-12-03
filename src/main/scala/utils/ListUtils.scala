package utils

object ListUtils {
  def append(list: List[(String, String)], key: String, value: String): List[(String, String)] = {
    (key, value) :: list
  }

  def singleElementList(key: String, value: String): List[(String, String)] = append(Nil, key, value)

  def getEmptyList: List[(String, String)] = Nil
}
