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
package net.smert.frameworkgl.utils;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MemoryUsage {

    private final TimeSpan memTimeSpan;

    public MemoryUsage() {
        memTimeSpan = new TimeSpan();
    }

    public void update() {
        long deltatime = memTimeSpan.diffNowToLastUpdate();
        if (deltatime < 1000000000L) {
            return;
        }

        long freeMem = Runtime.getRuntime().freeMemory();
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long usedMem = totalMem - freeMem;
        memTimeSpan.addToCurrentTime(1000000000L);
        System.out.println("Used memory: " + (usedMem * 100L) / maxMem + "% "
                + usedMem / 1024L / 1024L + "MB of " + maxMem / 1024L / 1024L + "MB");
    }

}
