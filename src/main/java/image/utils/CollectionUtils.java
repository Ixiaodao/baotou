package image.utils;

import java.util.Collection;

public class CollectionUtils {
    public static <T> boolean isEmpty (Collection<T> collection) {
        return collection == null || collection.size() <= 0;
    }

    public static <T> boolean isNotEmpty (Collection<T> collection) {
        return !isEmpty(collection);
    }


}
