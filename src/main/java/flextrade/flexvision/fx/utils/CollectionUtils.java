package flextrade.flexvision.fx.utils;

import java.util.Collections;
import java.util.List;

public class CollectionUtils {
    public static <T> List<T> toNotNullList(List<T> list) {
        return (list == null || list.size() == 0) ? Collections.<T>emptyList() : list;
    }
}
