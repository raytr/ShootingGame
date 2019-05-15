package game.shared.net.messages.commands;

import game.shared.net.messages.Command;

import java.util.HashMap;
import java.util.Map;

public enum CommandType {
    MOVE(-1){
        @Override
        public Command decode(byte[] bytes){
            return MoveCommand.decode(bytes);
        }
    },
    MOUSE_MOVE(0){
        @Override
        public Command decode(byte[] bytes){
            //@TODO
            return MoveCommand.decode(bytes);
        }
    },
    START_SHOOT(1){
        @Override
        public Command decode(byte[] bytes){
            //TODO
            return MoveCommand.decode(bytes);
        }
    },
    STOP_SHOOT(2){
        @Override
        public Command decode(byte[] bytes){
            //TODO
            return MoveCommand.decode(bytes);
        }
    };


    private final int value;
    private static final Map<Integer, CommandType> map = new HashMap<Integer, CommandType>();

    static {
        for (CommandType pt : CommandType.values()) {
            map.put(pt.value, pt);
        }
    }

    CommandType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CommandType valueOf(int i) {
        return map.get(i);
    }

    public abstract Command decode(byte[] bytes);
}
