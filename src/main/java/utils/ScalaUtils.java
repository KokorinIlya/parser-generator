package utils;

import scala.Option;
import scala.Tuple2;
import scala.collection.immutable.List;

public class ScalaUtils {
    public static List<Tuple2<String, String>> appendTupleToList(List<Tuple2<String, String>> list,
                                                                 String key, String value) {
        return ListUtils.appendTuple(list, key, value);
    }

    public static List<Tuple2<String, String>> singleTupleList(String key, String value) {
        return ListUtils.singleTupleList(key, value);
    }

    public static List<Tuple2<String, String>> emptyTupleList() {
        return ListUtils.getEmptyTupleList();
    }

    public static<T> List<T> appendToList(List<T> list, T elem) {
        return ListUtils.appendToList(list, elem);
    }

    public static<T> List<T> singleElementList(T element) {
        return ListUtils.singleElementList(element);
    }

    public static<T> List<T> emptyList() {
        return ListUtils.<T>getEmptyList();
    }

    public static<T> Option<T> emptyOption() {
        return Option.empty();
    }

    public static<T> Option<T> fullOption(T x) {
        return Option.apply(x);
    }
}
