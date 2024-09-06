package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {

    private int frameCount;                 // Counts ticks for change
    private int frameDelay;                 // frame delay 1-12 (You will have to play around with this)
    private int currentFrame;               // animations current frame
    private final int animationDirection;         // animation direction (i.e counting forward or backward)
    private final int totalFrames;                // total amount of frames for your animation
    private boolean playedOnce;
    private boolean stopped;                // has animations stopped

    private final ArrayList<Frame> frames = new ArrayList<>();    // Arraylist of frames

    public Animation(BufferedImage[] frames, int frameDelay) {
        this.frameDelay = frameDelay;
        this.stopped = true;

        this.playedOnce = false;

        for (BufferedImage frame : frames) {
            addFrame(frame, frameDelay);
        }

        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.animationDirection = 1;
        this.totalFrames = this.frames.size();

    }

    public int getTotalFrames(){
        return totalFrames;
    }

    public int getCurrentFrame(){
        return currentFrame;
    }

    public void start() {
        if (!stopped) {
            return;
        }

        if (frames.isEmpty()) {
            return;
        }

        stopped = false;
    }

    public boolean hasPlayedOnce() {
        return playedOnce;
    }

    public void stop() {
        if (frames.isEmpty()) {
            return;
        }

        stopped = true;
    }

    public void restart() {
        if (frames.isEmpty()) {
            return;
        }

        stopped = false;
        currentFrame = 0;
    }

    public void reset() {
        this.stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
        playedOnce = false;
    }

    public boolean isFinished() {
        return currentFrame >= totalFrames - 1;
    }

    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            System.err.println(STR."Invalid duration: \{duration}");
            throw new RuntimeException(STR."Invalid duration: \{duration}");
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }

    public BufferedImage getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    public void setCurrentFrame(int frame){
        currentFrame = frame;
    }

    public void update() {
        if (!stopped) {
            frameCount++;

            if (frameCount > frameDelay) {
                frameCount = 0;
                currentFrame += animationDirection;

                if (currentFrame > totalFrames - 1) {
                    currentFrame = 0;
                    playedOnce = true;
                }
                else if (currentFrame < 0) {
                    currentFrame = totalFrames - 1;
                }
            }
        }
    }

}