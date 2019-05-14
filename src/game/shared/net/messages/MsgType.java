package game.shared.net.messages;

import game.shared.net.Message;

import java.util.HashMap;
import java.util.Map;

public enum MsgType {
    ACK(-1){
        @Override
        public Message decode(byte[] bytes){
            return AckMsg.decode(bytes);
        }
    },
    LOGIN(0){
        @Override
        public Message decode(byte[] bytes){
           return LoginMsg.decode(bytes);
        }
    },
    LOGOUT(1){
        @Override
        public Message decode(byte[] bytes){
            return LogoutMsg.decode(bytes);
        }
    },
    CHAT_MESSAGE(2){
        @Override
        public Message decode(byte[] bytes){
            return ChatMsg.decode(bytes);
        }
    },
    GAME_STATE(3){
        @Override
        public Message decode(byte[] bytes){
            //TODO
            return LogoutMsg.decode(bytes);
        }
    },
    PLAYER_INTENT(6){
        @Override
        public Message decode(byte[] bytes){
            return PlayerIntentMsg.decode(bytes);
        }
    };



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

    public abstract Message decode(byte[] bytes);
}
