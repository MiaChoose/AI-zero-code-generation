package dev.langchain4j.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;

/**
 * 跨层共享的流取消注册表。
 * 使用 memoryId（本项目为 appId）关联取消检查器，供流式处理链路读取。
 */
public final class StreamingCancellationRegistry {

    private static final Map<Object, BooleanSupplier> CANCEL_CHECKER_MAP = new ConcurrentHashMap<>();
    private static final BooleanSupplier NEVER_CANCELLED = () -> false;

    private StreamingCancellationRegistry() {
    }

    public static void register(Object memoryId, BooleanSupplier cancelChecker) {
        if (memoryId == null || cancelChecker == null) {
            return;
        }
        CANCEL_CHECKER_MAP.put(memoryId, cancelChecker);
    }

    public static void unregister(Object memoryId) {
        if (memoryId == null) {
            return;
        }
        CANCEL_CHECKER_MAP.remove(memoryId);
    }

    public static boolean isCancelled(Object memoryId) {
        return resolve(memoryId).getAsBoolean();
    }

    public static BooleanSupplier resolve(Object memoryId) {
        if (memoryId == null) {
            return NEVER_CANCELLED;
        }
        BooleanSupplier cancelChecker = CANCEL_CHECKER_MAP.get(memoryId);
        if (cancelChecker == null) {
            return NEVER_CANCELLED;
        }
        return cancelChecker;
    }
}
