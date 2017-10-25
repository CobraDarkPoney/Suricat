package ladysnake.models;

import java.util.ArrayList;
import java.util.List;

/**A helping class that holds all available models created
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class AvailableModels {
    /**The list of available models*/
    public final static List<DBModel> models = new ArrayList<>();

    /**A handy method to add a model to the list of available models
     * @param model being the {@link DBModel} to add
     * @return TRUE if added, FALSE otherwise
     */
    public final static boolean add(DBModel model){
        return AvailableModels.models.add(model);
    }

    /**A handy method that retrieves a model from its name*/
    public final static DBModel get(String name){
        return AvailableModels.models.stream()
                .filter(model -> model.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
