package net.smert.jreactphysics3d.framework;

import net.smert.jreactphysics3d.framework.utils.TimeSpan;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Timer {

    private final static int FRAME_SLOTS = 100;
    private final static long ONE_NANO_SECOND = 1000000000L;

    private boolean isGameTick;
    private boolean isRenderTick;
    private double lostGameTicks;
    private double lostRenderTicks;
    private double remainingGameTicks;
    private double remainingRenderTicks;
    private float delta;
    private float totalTime;
    private int frameCounter;
    private int gameTicksPerSecond;
    private int renderTicksPerSecond;
    private long currentElapsedTimeDifference;
    private final Configuration config;
    private final FrameAve frameAve;
    private final TimeSpan elapsedTimeSpan;
    private final TimeSpan totalTimeSpan;

    public Timer(Configuration config) {
        this.config = config;
        frameAve = new FrameAve(FRAME_SLOTS);
        elapsedTimeSpan = new TimeSpan();
        totalTimeSpan = new TimeSpan();
        reset();
    }

    public double getLostGameTicks() {
        return lostGameTicks;
    }

    public double getLostRenderTicks() {
        return lostRenderTicks;
    }

    public double getRemainingGameTicks() {
        return remainingGameTicks;
    }

    public double getRemainingRenderTicks() {
        return remainingRenderTicks;
    }

    public float getDelta() {
        return delta;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public int getFrameCounter() {
        return frameCounter;
    }

    public int getGameTicksPerSecond() {
        return gameTicksPerSecond;
    }

    public int getRenderTicksPerSecond() {
        return renderTicksPerSecond;
    }

    public boolean isGameTick() {
        return isGameTick;
    }

    public boolean isRenderTick() {
        return isRenderTick;
    }

    public final void reset() {
        isGameTick = false;
        isRenderTick = false;
        lostGameTicks = 0;
        lostRenderTicks = 0;
        remainingGameTicks = 0;
        remainingRenderTicks = 0;
        delta = 0;
        totalTime = 0;
        frameCounter = 0;
        gameTicksPerSecond = 0;
        renderTicksPerSecond = 0;
        currentElapsedTimeDifference = 0;
        frameAve.init(ONE_NANO_SECOND / 60);
        elapsedTimeSpan.reset();
        totalTimeSpan.reset();
    }

    public void update() {

        // Update the elapsed time since the last frame and increase the counter
        elapsedTimeSpan.update();
        frameCounter++;

        // The number of nanoseconds since the last update. Save the difference for computing the average.
        currentElapsedTimeDifference = elapsedTimeSpan.diffLastUpdateToLastTime();
        delta = (float) elapsedTimeSpan.diffLastUpdateToLastTimeDouble();
        frameAve.add(currentElapsedTimeDifference);

        // Get the total time since we started
        totalTime = (float) totalTimeSpan.diffNowToLastUpdateDouble();

        // Get the number of game ticks per second and render ticks based on the frame rate average
        gameTicksPerSecond = config.gameTicksPerSecond;
        renderTicksPerSecond = (int) (ONE_NANO_SECOND / frameAve.avg());

        assert (gameTicksPerSecond > 0);
        assert (renderTicksPerSecond > 0);

        // We want the total time for each type of tick in the number of nanoseconds per tick
        double gameTick = ONE_NANO_SECOND / gameTicksPerSecond;
        double renderTick = ONE_NANO_SECOND / renderTicksPerSecond;

        // The current time difference in nanoseconds is used to compute a percent of a tick that
        // has elapsed since the last frame. 
        remainingGameTicks += currentElapsedTimeDifference / gameTick;
        remainingRenderTicks += currentElapsedTimeDifference / renderTick;

        // Check to see if we have enough remaining time to take a tick
        isGameTick = (remainingGameTicks >= 1.0D);
        isRenderTick = (remainingRenderTicks >= 1.0D);

        // We are only allowed to take 1 tick per frame
        if (isGameTick) {
            remainingGameTicks -= 1.0D;
        }
        if (isRenderTick) {
            remainingRenderTicks -= 1.0D;
        }

        // If we have any more than two ticks remaining then it is lost. If things are going well the tiny fraction
        // that will be added to the remaining ticks next frame shouldn't keep us from losing time. Render ticks are
        // going to be much more sensitive to this.
        while (remainingGameTicks > 2.0D) {
            lostGameTicks += 1.0D;
            remainingGameTicks -= 1.0D;
        }
        while (remainingRenderTicks > 2.0D) {
            lostRenderTicks += 1.0D;
            remainingRenderTicks -= 1.0D;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("isGameTick=");
        sb.append(isGameTick);
        sb.append(", ");
        sb.append("isRenderTick=");
        sb.append(isRenderTick);
        sb.append(", ");
        sb.append("lostGameTicks=");
        sb.append(lostGameTicks);
        sb.append(", ");
        sb.append("lostRenderTicks=");
        sb.append(lostRenderTicks);
        sb.append(", ");
        sb.append("remainingGameTicks=");
        sb.append(remainingGameTicks);
        sb.append(", ");
        sb.append("remainingRenderTicks=");
        sb.append(remainingRenderTicks);
        sb.append(", ");
        sb.append("delta=");
        sb.append(delta);
        sb.append(", ");
        sb.append("totalTime=");
        sb.append(totalTime);
        sb.append(", ");
        sb.append("frameCounter=");
        sb.append(frameCounter);
        sb.append(", ");
        sb.append("gameTicksPerSecond=");
        sb.append(gameTicksPerSecond);
        sb.append(", ");
        sb.append("renderTicksPerSecond=");
        sb.append(renderTicksPerSecond);
        sb.append(", ");
        sb.append("currentElapsedTimeDifference=");
        sb.append(currentElapsedTimeDifference);
        sb.append("}");
        return sb.toString();
    }

    private static class FrameAve {

        private final long[] slots;
        private int offset;

        public FrameAve(int count) {
            this.slots = new long[count];
            this.offset = 0;
        }

        public void add(long value) {
            this.slots[this.offset] = value;
            this.offset = (this.offset + 1) % this.slots.length;
        }

        public long avg() {
            long sum = 0;
            for (int i = 0; i < this.slots.length; i++) {
                sum += this.slots[i];
            }
            return sum / this.slots.length;
        }

        public void init(long value) {
            for (int i = 0; i < this.slots.length; i++) {
                this.slots[i] = value;
            }
        }

    }

}
