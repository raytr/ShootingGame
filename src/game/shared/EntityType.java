package game.shared;
import java.util.HashMap;
import java.util.Map;

public enum EntityType {
    PLAYER(0),BULLET(1);

    private final int value;
    private static final Map<Integer, EntityType> map = new HashMap<Integer, EntityType>();

    static {
        for (EntityType pt : EntityType.values()) {
            map.put(pt.value, pt);
        }
    }

    EntityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EntityType valueOf(int i) {
        return map.get(i);
    }
}
