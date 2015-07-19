/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.smert.frameworkgl;

import net.smert.frameworkgl.utils.TimeSpan;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Timer {

    private final static int FRAME_SLOTS = 100;
    public final static long ONE_NANO_SECOND = 1000000000L;

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
    private final FrameAve frameAve;
    private final TimeSpan elapsedTimeSpan;
    private final TimeSpan totalTimeSpan;

    public Timer() {
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

    public long getCurrentElapsedTimeDifference() {
        return currentElapsedTimeDifference;
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
        frameAve.init(ONE_NANO_SECOND / 60L);
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
        gameTicksPerSecond = Fw.config.gameTicksPerSecond;
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
        isGameTick = (remainingGameTicks >= 1D);
        isRenderTick = (remainingRenderTicks >= 1D);

        // We are only allowed to take 1 tick per frame
        if (isGameTick) {
            remainingGameTicks -= 1D;
        }
        if (isRenderTick) {
            remainingRenderTicks -= 1D;
        }

        // If we have any more than two ticks remaining then it is lost. If things are going well the tiny fraction
        // that will be added to the remaining ticks next frame shouldn't keep us from losing time. Render ticks are
        // going to be much more sensitive to this.
        while (remainingGameTicks > 2D) {
            lostGameTicks += 1D;
            remainingGameTicks -= 1D;
        }
        while (remainingRenderTicks > 2D) {
            lostRenderTicks += 1D;
            remainingRenderTicks -= 1D;
        }
    }

    @Override
    public String toString() {
        return "(isGameTick= " + isGameTick + " isRenderTick= " + isRenderTick
                + " lostGameTicks= " + lostGameTicks + " lostRenderTicks= " + lostRenderTicks
                + " remainingGameTicks= " + remainingGameTicks + " remainingRenderTicks= " + remainingRenderTicks
                + " delta= " + delta + " totalTime= " + totalTime
                + " frameCounter= " + frameCounter + " gameTicksPerSecond= " + gameTicksPerSecond
                + " renderTicksPerSecond= " + renderTicksPerSecond
                + " currentElapsedTimeDifference= " + currentElapsedTimeDifference + ")";
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
            long sum = 0L;
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
