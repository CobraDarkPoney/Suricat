package ladysnake.helpers.utils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public abstract class Range {
    public static List<Integer> make(int lowerBound, int upperBound){
        return make(lowerBound, upperBound, 1);
    }

    public static List<Integer> make(int lowerBound, int upperBound, int step){
        if(step <= 0)
            throw new IllegalArgumentException("The step of a range must be > 0");

        if(lowerBound >= upperBound)
            throw new IllegalArgumentException("The lower bound must be  < the upper bound");

        List<Integer> ret = new ArrayList<>();

        for(int item = lowerBound ; item < upperBound ; item+=step)
            ret.add(item);

        return ret;
    }

    public static List<Character> make(char lowerBound, char upperBound){
        return make(lowerBound, upperBound, 1);
    }

    public static List<Character> make(char lowerBound, char upperBound, int step){
        if(step <= 0)
            throw new IllegalArgumentException("The step of a range must be > 0");

        if(lowerBound >= upperBound)
            throw new IllegalArgumentException("The lower bound must be  < the upper bound");

        List<Character> ret = new ArrayList<>();

        for(char item = lowerBound ; item < upperBound ; item+=step)
            ret.add(item);

        return ret;
    }
}
