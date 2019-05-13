package game.shared.net.packets;

import java.util.HashMap;
import java.util.Map;

public enum MsgType {
    LOGIN(0), LOGOUT(1), CHAT_MESSAGE(2), GAME_STATE(3), ACK(4), CONTROLS(5);
    private final int value;
    private static final Map<Integer, MsgType> map = new HashMap<Integer, MsgType>();

    static {
        for (MsgType pt : MsgType.values()) {
            map.put(pt.value, pt);
        }
    }

    MsgType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MsgType valueOf(int i) {
        return map.get(i);
    }
}
