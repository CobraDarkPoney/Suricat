package ladysnake.models;

import com.google.gson.JsonElement;
import ladysnake.helpers.json.I_JsonSerializable;
import ladysnake.helpers.utils.I_Stringify;

/**An interface representing a database's granule used in transactions
 * @author Ludwig GUERIN
 */
public interface DBGranule extends I_Stringify, I_JsonSerializable{
    String getName();
}
