package net.smert.jreactphysics3d.framework.utils;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TimeSpan {

    public final static double ONE_NANO_SECOND = 1000000000.0D;

    private long currentTime;
    private long lastTime;

    public TimeSpan() {
        reset();
    }

    public void addToCurrentTime(long time) {
        currentTime += time;
    }

    public double diffLastUpdateToLastTimeDouble() {
        return diffLastUpdateToLastTime() / ONE_NANO_SECOND;
    }

    public double diffNowToLastUpdateDouble() {
        return diffNowToLastUpdate() / ONE_NANO_SECOND;
    }

    public long diffLastUpdateToLastTime() {
        return currentTime - lastTime;
    }

    public long diffNowToLastUpdate() {
        return System.nanoTime() - currentTime;
    }

    public final void reset() {
        currentTime = System.nanoTime();
        lastTime = System.nanoTime();
    }

    public void update() {
        lastTime = currentTime;
        currentTime = System.nanoTime();
    }

}
