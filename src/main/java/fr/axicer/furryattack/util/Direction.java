package fr.axicer.furryattack.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Direction {

    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    TOP_RIGHT,
    TOP_LEFT,
    BOTTOM_RIGHT,
    BOTTOM_LEFT,
    ;

    public static Direction getFromOrdinal(int ordinal) {
        return Stream.of(values())
                .filter(orientation -> orientation.ordinal() == ordinal)
                .findFirst()
                .orElse(null);
    }

    public static List<Direction> getAllDirections(){
        return Arrays.asList(values());
    }

    public static List<Direction> getAllDirectionsExcept(Direction... dirs){
        return Stream.of(values())
                .filter(dir -> !Arrays.asList(dirs).contains(dir))
                .collect(Collectors.toList());
    }

}
