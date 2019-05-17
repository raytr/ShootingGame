package game.shared.net.messages;

import game.server.model.GameMap;
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
    ENTITY_STATE(3){
        @Override
        public Message decode(byte[] bytes){
            return EntityStateMsg.decode(bytes);
        }
    },
    ENTITY_CREATE(4){
        @Override
        public Message decode(byte[] bytes){
            return EntityCreateMsg.decode(bytes);
        }
    },
    ENTITY_DELETE(5){
        @Override
        public Message decode(byte[] bytes){
            return EntityDeleteMsg.decode(bytes);
        }
    },
    PLAYER_COMMAND(6){
        @Override
        public Message decode(byte[] bytes){
            return CommandMsg.decode(bytes);
        }
    },
    GAME_MAP(7){
        @Override
        public Message decode(byte[] bytes){
            return GameMapMsg.decode(bytes);
        }
    },
    PLAYER_STATE(8){
        @Override
        public Message decode(byte[] bytes){
            return PlayerStateMsg.decode(bytes);
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
