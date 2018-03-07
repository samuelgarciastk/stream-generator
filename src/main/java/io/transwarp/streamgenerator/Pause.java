package io.transwarp.streamgenerator;

/**
 * Author: stk
 * Date: 2018/3/5
 */
public enum Pause {
    INSTANCE;
    private volatile boolean isPaused = false;

    public void setIsPaused(boolean pause) {
        isPaused = pause;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
