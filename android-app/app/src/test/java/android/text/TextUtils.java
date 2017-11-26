package android.text;

/**
 * Created by jimo on 17-11-26.
 * 为了解决依赖，自己实现
 */

public class TextUtils {
    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        for (Object token : tokens) {
            sb.append(delimiter).append(token);
        }
        return sb.substring(1);
    }
}
