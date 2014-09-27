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
