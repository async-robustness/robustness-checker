/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.content.ComponentName;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    public static class RunningTaskInfo {
        public static final ComponentName baseActivity = new ComponentName();

        public RunningTaskInfo() {
        }
    }

    public static class MemoryInfo {
        public long availMem;
        public long totalMem;
        public long threshold;
        public boolean lowMemory;
    }

    private static final MemoryInfo mMemoryInfo = new MemoryInfo();

    public MemoryInfo getMemoryInfo(MemoryInfo memoryInfo) {
        return mMemoryInfo;
    }

    public List<RunningTaskInfo> getRunningTasks(int max) {
        return new ArrayList<RunningTaskInfo>();
    }

    public int getLauncherLargeIconDensity() {
        return 0;
    }
}
