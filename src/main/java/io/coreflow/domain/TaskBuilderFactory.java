package io.coreflow.domain;

import io.coreflow.domain.payloads.TaskPayload;
import io.coreflow.domain.task.RetryPolicy;
import io.coreflow.domain.task.TaskPriority;
import io.coreflow.domain.task.WorkloadType;

public final class TaskBuilderFactory {

    private static final TaskBuilderFactory INSTANCE = new TaskBuilderFactory();

    private TaskBuilderFactory() {
    }

    public static TaskBuilderFactory getInstance() {
        return INSTANCE;
    }

    public <T extends TaskPayload> TaskBuilder<T> createTaskBuilder(
            T payload,
            TaskPriority priority,
            WorkloadType workloadType,
            Long timeout,
            RetryPolicy retryPolicy) {
        return new TaskBuilder<>(payload, priority, workloadType, timeout, retryPolicy);
    }
}
