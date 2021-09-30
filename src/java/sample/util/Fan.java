package sample.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Fan {
    _1,
    _2,
    _3,
    _4,
    _5,
    _6;

    private static final List<Fan> FUN3_FANS = new ArrayList<>();
    private static final List<Fan> FUN6_FANS = new ArrayList<>();
    private static final List<Fan> FUN10_FANS = new ArrayList<>();
    public static final Map<FUN, List<Fan>> FAN_LISTS = new HashMap<>();

    static {
        FUN3_FANS.add(_1);
        FUN3_FANS.add(_2);
        FUN6_FANS.add(_1);
        FUN6_FANS.add(_2);
        FUN6_FANS.add(_3);
        FUN6_FANS.add(_4);
        FUN10_FANS.add(_1);
        FUN10_FANS.add(_2);
        FUN10_FANS.add(_3);
        FUN10_FANS.add(_4);
        FUN10_FANS.add(_5);
        FUN10_FANS.add(_6);
        FAN_LISTS.put(FUN._3, FUN3_FANS);
        FAN_LISTS.put(FUN._3ZIP, FUN3_FANS);
        FAN_LISTS.put(FUN._6, FUN6_FANS);
        FAN_LISTS.put(FUN._6ZIP, FUN6_FANS);
        FAN_LISTS.put(FUN._10, FUN10_FANS);
        FAN_LISTS.put(FUN._10ZIP, FUN10_FANS);
    }
}