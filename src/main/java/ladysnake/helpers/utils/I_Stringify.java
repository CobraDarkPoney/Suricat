package ladysnake.helpers.utils;

import java.util.regex.Pattern;

/**An interface used to declare objects that can use a stringify method
 * @author Ludwig GUERIN
 */
public interface I_Stringify extends I_MightNoNullParams{
    final static Pattern TAB_RE = Pattern.compile("^\\t*$");
    final static String STRINGIFY_ERROR_MESSAGE = "<stringify> TAB LEVEL NOT TABS  </stringify>";;

    /**A method used to check if a {@link String} is only composed of tabulations or not
     * @param str being the {@link String} to test upon
     * @return TRUE if only composed of tabulations, FALSE otherwise
     */
    static boolean isTab(String str){
        return TAB_RE.matcher(str).matches();
    }

    /**A method used to stringify in a fancy way (while controlling indentation levels via tabulations)
     * @param tabLevel being a String constituted only of several tabulation characters
     * @return a stringified version of this {@link I_Stringify}
     */
    String stringify(String tabLevel);

    default String stringify(){ return this.stringify(""); }
}
