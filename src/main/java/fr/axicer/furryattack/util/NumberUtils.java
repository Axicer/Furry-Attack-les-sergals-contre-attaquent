package fr.axicer.furryattack.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;

public class NumberUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumberUtils.class);

    public static float[] toFloatArray(List<Float> list){
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Try to parse a String to a Integer returns Integer.MIN_VALUE if failed
     * @param obj String to parse
     * @return number parsed or Integer.MIN_VALUE if failed to parse
     */
    public static int parseInt(String obj){
        int number = 0;
        try{
            number = Integer.parseInt(obj);
        }catch (NumberFormatException ex){
            LOGGER.error("Unable to parse {} as int, returned -1", obj);
            number = Integer.MIN_VALUE;
        }
        return number;
    }

    /**
     * Check if any of the value given are equals to the comp value
     * @param comp reference value
     * @param values values to check
     * @return true if any of this values is equals to comp, false either
     */
    public static boolean isAny(int comp, int... values){
        return IntStream.of(values).anyMatch(value -> value == comp);
    }
}
