package net.smert.jreactphysics3d.framework.utils;

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

        if (deltatime >= 1000000000L) {
            long freeMem = Runtime.getRuntime().freeMemory();
            long maxMem = Runtime.getRuntime().maxMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            long usedMem = totalMem - freeMem;
            memTimeSpan.addToCurrentTime(1000000000L);
            System.out.println("Used memory: " + (usedMem * 100L) / maxMem + "% "
                    + usedMem / 1024L / 1024L + "MB of " + maxMem / 1024L / 1024L + "MB");
        }
    }

}
