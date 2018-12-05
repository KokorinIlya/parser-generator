package utils

object ListUtils {
  def appendTuple(list: List[(String, String)], key: String, value: String): List[(String, String)] = {
    (key, value) :: list
  }

  def singleTupleList(key: String, value: String): List[(String, String)] = appendTuple(Nil, key, value)

  def getEmptyTupleList: List[(String, String)] = Nil

  def appendToList[T](list: List[T], elem: T): List[T] = {
    elem :: list
  }

  def singleElementList[T](elem: T): List[T] = appendToList(Nil, elem)

  def getEmptyList[T]: List[T] = Nil
}
