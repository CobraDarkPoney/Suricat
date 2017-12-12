package ladysnake.helpers.utils;

public interface I_MightNoNullParams {
    default void assertParamsAreNotNull(Object... params){
        if(!Params.noNull(params))
            throw new IllegalArgumentException("Parameters given to this method must not be null");
    }

    public static void assertNoneNull(Object... params){
        if(!Params.noNull(params))
            throw new IllegalArgumentException("Parameters given to this method must not be null");
    }
}
