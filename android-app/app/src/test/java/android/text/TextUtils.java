package android.text;

/**
 * Created by jimo on 17-11-26.
 * 为了解决依赖，自己实现
 */

public class TextUtils {
    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = tokens.length - 1; i >= 0; i--) {
            sb.append(delimiter).append(tokens[i]);
        }
        return sb.substring(1);
    }
}
