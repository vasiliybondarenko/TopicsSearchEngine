package infrascructure.data.util;

import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/24/13
 * Time: 5:12 PM
 * Project: IntelligentSearch
 */
public class ArrayFormatter<T> implements Function<T[], String> {

    private final String separator;

    public ArrayFormatter(String separator) {
        this.separator = separator;
    }

    @Override
    public String apply(T[] array) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]).append(separator);
        }
        return sb.toString();
    }
}
