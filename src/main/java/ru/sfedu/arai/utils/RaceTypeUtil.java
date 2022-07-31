package ru.sfedu.arai.utils;

import ru.sfedu.arai.enums.RaceType;

public class RaceTypeUtil {
    public static RaceType raceTypeParser(String x) throws Exception {
        switch(x){
            case "TRIATHLON": return RaceType.TRIATHLON;
            default: throw new Exception("raceTypeParser error, incorrect race type");
        }
    }
}
