package ladysnake.controllers;

import ladysnake.helpers.utils.I_MightNoNullParams;
import ladysnake.views.A_View;
import ladysnake.views.ViewsManager;


/**An abstract class factorizing the shared behaviors of controllers
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public abstract class A_Controller implements I_MightNoNullParams{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected A_View view;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public A_Controller(A_View view){
        this.assertParamsAreNotNull(view);

        this.view = view;
        this.addListeners();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Retrieves the {@link ViewsManager} tied to the {@link A_View} of this {@link A_Controller}
     * @return {@link ViewsManager}
     */
    public final ViewsManager getManager(){
        return this.view.getManager();
    }

    public abstract void addListeners();
}
