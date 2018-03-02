package ladysnake.controllers;

import ladysnake.helpers.log.Logger;
import ladysnake.models.ModelsManager;
import ladysnake.views.ViewsManager;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class ControllersManager {
    protected Map<String, A_Controller> controllers;
    protected ViewsManager vm;
    protected ModelsManager modelsManager;

    public ControllersManager(ViewsManager vm, ModelsManager mm){
        this.controllers = new HashMap<>();
        this.vm = vm;
        this.modelsManager = mm;
    }

    public  ControllersManager addController(String name, A_Controller controller){
        Logger.triggerEvent(Logger.VERBOSE, "Adding/replacing a controller : " + name);

        if(this.hasController(name))
            this.controllers.replace(name, controller);
        else
            this.controllers.put(name, controller);

        return this;
    }

    public boolean hasController(String name){ return this.controllers.containsKey(name); }

    public A_Controller getController(String name){ return this.controllers.get(name); }

    public ViewsManager getViewsManager(){ return this.vm; }

    public ModelsManager getModelsManager() { return modelsManager; }

    public ControllersManager setModelsManager(ModelsManager manager){
        this.modelsManager = manager;
        return this;
    }
}
