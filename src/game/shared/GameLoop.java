package game.shared;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Runs the main game logic with a fixed-timestep game loop
public class GameLoop extends Thread {
    private final int FPS;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final Runnable updater;
    public GameLoop(Runnable updater,int FPS){
        this.FPS = FPS;
        this.updater = updater;
    }

    //Handles fixed-timestep
    @Override
    public void run() {
        executorService.scheduleAtFixedRate(updater,0,1000/FPS, TimeUnit.MILLISECONDS);
    }
    public void stopRunning(){
        executorService.shutdown();
    }

}
