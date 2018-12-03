package utils;

import scala.Tuple2;
import scala.collection.immutable.List;

public class ScalaUtils {
    public static List<Tuple2<String, String>> appendToList(List<Tuple2<String, String>> list,
                                                              String key, String value) {
        return ListUtils.append(list, key, value);
    }

    public static List<Tuple2<String, String>> singleElementList(String key, String value) {
        return ListUtils.singleElementList(key, value);
    }

    public static List<Tuple2<String, String>> emptyList() {
        return ListUtils.getEmptyList();
    }
}
