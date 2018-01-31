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

    protected ControllersManager controllersManager;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public A_Controller(A_View view, ControllersManager cm){
        this.assertParamsAreNotNull(view, cm);

        this.view = view;
        this.controllersManager = cm;
        this.addListeners();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Retrieves the {@link ViewsManager} tied to the {@link A_View} of this {@link A_Controller}
     * @return {@link ViewsManager}
     */
    public final ViewsManager getViewsManager(){
        return this.view.getManager();
    }

    public final ControllersManager getControllersManager(){ return this.controllersManager; }

    public abstract void addListeners();
}
