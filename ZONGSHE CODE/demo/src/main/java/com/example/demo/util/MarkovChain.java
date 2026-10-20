package com.example.demo.util;

import com.example.demo.enums.VehicleState;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class MarkovChain {
    
    private static final Random random = new Random(20261020); 

    private static final Map<VehicleState, int[]> TRANSITION_TABLE = new EnumMap<>(VehicleState.class);

    static {
        // 枚举顺序：TO_PICKUP(0), LOADING(1), TRANSPORTING(2), UNLOADING(3), WAITING(4), IDLE(5)
        TRANSITION_TABLE.put(VehicleState.TO_PICKUP, new int[]{500, 9000, 0, 0, 500, 0});
        TRANSITION_TABLE.put(VehicleState.LOADING, new int[]{0, 200, 9500, 200, 100, 0});
        TRANSITION_TABLE.put(VehicleState.TRANSPORTING, new int[]{0, 0, 200, 9500, 300, 0});
        TRANSITION_TABLE.put(VehicleState.UNLOADING, new int[]{0, 0, 0, 300, 200, 9500});
        TRANSITION_TABLE.put(VehicleState.WAITING, new int[]{0, 9000, 0, 0, 1000, 0});
        TRANSITION_TABLE.put(VehicleState.IDLE, new int[]{0, 0, 0, 0, 0, 10000});
    }

    public static VehicleState chooseNextState(VehicleState currentState) {
        int[] weights = TRANSITION_TABLE.get(currentState);
        if (weights == null) return currentState;

        int r = random.nextInt(10000); // 0 到 9999
        int cumulative = 0;

        for (VehicleState state : VehicleState.values()) {
            cumulative += weights[state.ordinal()];
            if (r < cumulative) {
                return state;
            }
        }
        return currentState;
    }
}