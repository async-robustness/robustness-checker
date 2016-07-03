/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app.usage;

import java.util.Collections;
import java.util.List;

public class UsageStatsManager {

    public static final int INTERVAL_DAILY = 0;
    public static final int INTERVAL_WEEKLY = 1;
    public static final int INTERVAL_MONTHLY = 2;
    public static final int INTERVAL_YEARLY = 3;
    public static final int INTERVAL_BEST = 4;

    public List<UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) {
        return Collections.emptyList();
    }

}
