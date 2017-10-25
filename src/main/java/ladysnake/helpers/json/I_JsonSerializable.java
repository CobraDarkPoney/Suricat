package ladysnake.helpers.json;

import com.google.gson.JsonElement;

/**An interface used to designate classes that can be serialized to the JSON format
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"WeakerAccess","unused"})
public interface I_JsonSerializable {
    /**A class method that every class implementing {@link I_JsonSerializable} should implement
     * @param element being the {@link JsonElement} (or subclass) to read from
     * @return a {@link I_JsonSerializable} (or subclass) created from the given {@link JsonElement} (or subclass)
     */
    static I_JsonSerializable fromJson(JsonElement element){
        return null;
    }

    /**Creates a {@link JsonElement} from this {@link I_JsonSerializable}
     * @return a {@link JsonElement} that is the serialized form of this {@link I_JsonSerializable}
     */
    JsonElement toJson();
}
