package sample.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Slot {
    _1,
    _2,
    _3,
    _4,
    _5,
    _6,
    _7,
    _8,
    _9,
    _10,
    _11,
    _12,
    _13;

    private static final List<Slot> V3_SLOTS = new ArrayList<>();
    private static final List<Slot> V6_SLOTS = new ArrayList<>();
    private static final List<Slot> V10_SLOTS = new ArrayList<>();
    public static final Map<Crate, List<Slot>> SLOT_LISTS = new HashMap<>();

    static {
        V3_SLOTS.add(_1);
        V3_SLOTS.add(_2);
        V3_SLOTS.add(_3);
        V6_SLOTS.add(_1);
        V6_SLOTS.add(_2);
        V6_SLOTS.add(_3);
        V6_SLOTS.add(_4);
        V6_SLOTS.add(_5);
        V6_SLOTS.add(_6);
        V6_SLOTS.add(_7);
        V10_SLOTS.add(_1);
        V10_SLOTS.add(_2);
        V10_SLOTS.add(_3);
        V10_SLOTS.add(_4);
        V10_SLOTS.add(_5);
        V10_SLOTS.add(_6);
        V10_SLOTS.add(_7);
        V10_SLOTS.add(_8);
        V10_SLOTS.add(_9);
        V10_SLOTS.add(_10);
        V10_SLOTS.add(_11);
        V10_SLOTS.add(_12);
        V10_SLOTS.add(_13);
        SLOT_LISTS.put(Crate.V3, V3_SLOTS);
        SLOT_LISTS.put(Crate.V6, V6_SLOTS);
        SLOT_LISTS.put(Crate.V10, V10_SLOTS);
    }
}