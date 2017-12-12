package ladysnake.models;

import ladysnake.helpers.utils.I_MightNoNullParams;

import java.util.ArrayList;
import java.util.List;

/**A helping class that holds all available models created
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"WeakerAccess","unused"})
public abstract class AvailableGranules {
    /**The list of available models*/
    public static List<DBGranule> granules = new ArrayList<>();

    /**A handy method to add a model to the list of available models
     * @param model being the {@link DBGranule} to add
     * @return TRUE if added, FALSE otherwise
     */
    public static boolean add(DBGranule model){
        I_MightNoNullParams.assertNoneNull(model);

        return AvailableGranules.granules.add(model);
    }

    /**A handy method that retrieves a model from its name
     *@param name being the name of the DBGranule to retrieve
     * @return the {@link DBGranule} if found, NULL otherwise
     */
    public static DBGranule get(String name){
        I_MightNoNullParams.assertNoneNull(name);

        return AvailableGranules.granules.stream()
                .filter(granule -> granule.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
