package ladysnake.helpers.utils;

public abstract class Params {
    public static boolean noNull(Object... params){
        if(params == null)
            return false;

        for(Object param : params){
            if(param == null)
                return false;
        }

        return true;
    }
}
