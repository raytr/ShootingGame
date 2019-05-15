package game.client;

import game.client.net.ClientReceivePacketHandler;
import javafx.application.Platform;

public class ClientGameLoopRunnable implements Runnable {
    private boolean cameraInit = false;
    Playfield p;
    Game g;
    long lastTimeNS = System.nanoTime();
    public ClientGameLoopRunnable(Game g,Playfield p){
        this.g= g;
        this.p =p;
    }
    @Override
    public void run() {
        long newTime = System.nanoTime();
        long dt= System.nanoTime() - lastTimeNS;
        lastTimeNS = newTime;
        for (Sprite s : p.getSpriteList()){
            s.update(dt/1e9);
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!cameraInit){
                    for (Sprite s : p.getSpriteList()){
                        if (s.getId() == g.getPlayerNum()){
                            p.bindCameraToSprite(s);
                            cameraInit = true;
                            break;
                        }
                    }
                }
                p.getGameCamera().update();
                p.draw();
            }
        });
    }
}
