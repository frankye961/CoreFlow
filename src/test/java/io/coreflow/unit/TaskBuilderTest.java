package io.coreflow.unit;

import io.coreflow.domain.TaskBuilder;
import io.coreflow.domain.TaskBuilderFactory;
import io.coreflow.domain.payloads.PrimeCalculationPayload;
import io.coreflow.domain.task.RetryPolicy;
import io.coreflow.domain.task.TaskDefinition;
import io.coreflow.domain.task.TaskId;
import io.coreflow.domain.task.TaskPriority;
import io.coreflow.domain.task.TaskStatus;
import io.coreflow.domain.task.WorkloadType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskBuilderTest {

    @Test
    void factoryIsASingletonAndBuildAppliesDefaults() {
        Map<String, String> metadata = new HashMap<>(Map.of("source", "original"));
        PrimeCalculationPayload payload = new PrimeCalculationPayload(100);

        Instant beforeBuild = Instant.now();
        TaskDefinition<PrimeCalculationPayload> task = builder(payload, TaskPriority.HIGH, 5_000L, new RetryPolicy(3))
                .withMetadata(metadata)
                .build();
        Instant afterBuild = Instant.now();

        assertThat(TaskBuilderFactory.getInstance()).isSameAs(TaskBuilderFactory.getInstance());
        assertThat(task.getTaskId()).isNotNull();
        assertThat(task.getTaskPayload()).isSameAs(payload);
        assertThat(task.getScheduledExecutionStartTime()).isEqualTo(task.getCreationTimestamp());
        assertThat(task.getCreationTimestamp()).isBetween(beforeBuild, afterBuild);
        assertThat(task.getMetadata()).containsExactly(Map.entry("source", "original"));

        metadata.put("source", "changed");

        assertThat(task.getMetadata()).containsExactly(Map.entry("source", "original"));
    }

    @Test
    void buildPreservesOptionalValues() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        Instant creationTime = Instant.parse("2026-01-01T10:15:30Z");
        Instant scheduledTime = Instant.parse("2026-01-02T10:15:30Z");
        Instant endTime = Instant.parse("2026-01-02T10:16:30Z");
        RetryPolicy retryPolicy = new RetryPolicy(3);
        PrimeCalculationPayload payload = new PrimeCalculationPayload(100);

        TaskDefinition<PrimeCalculationPayload> task = builder(payload, TaskPriority.HIGH, 5_000L, retryPolicy)
                .withTaskId(taskId)
                .withTaskStatus(TaskStatus.SCHEDULED)
                .withCreationTimestamp(creationTime)
                .withScheduledExecutionStartTime(scheduledTime)
                .withScheduledExecutionEndTime(endTime)
                .withIdempotencyKey("request-42")
                .withMetadata(Map.of("key", "value"))
                .build();

        assertThat(task.getTaskId()).isEqualTo(taskId);
        assertThat(task.getTaskPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.SCHEDULED);
        assertThat(task.getWorkloadType()).isEqualTo(WorkloadType.CPU_BOUND);
        assertThat(task.getScheduledExecutionStartTime()).isEqualTo(scheduledTime);
        assertThat(task.getScheduledExecutionEndTime()).isEqualTo(endTime);
        assertThat(task.getTimeout()).isEqualTo(5_000L);
        assertThat(task.getRetryPolicy()).isEqualTo(retryPolicy);
        assertThat(task.getIdempotencyKey()).isEqualTo("request-42");
        assertThat(task.getCreationTimestamp()).isEqualTo(creationTime);
        assertThat(task.getMetadata()).containsExactly(Map.entry("key", "value"));
    }

    @Test
    void buildCannotBeCalledTwice() {
        TaskBuilder<PrimeCalculationPayload> builder = builder(
                new PrimeCalculationPayload(10), TaskPriority.NORMAL, 5_000L, new RetryPolicy(1));

        builder.build();

        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("TaskBuilder cannot be reused after build");
    }

    @ParameterizedTest
    @EnumSource(TaskPriority.class)
    void buildPreservesEachPriority(TaskPriority priority) {
        assertThat(builder(new PrimeCalculationPayload(10), priority, 5_000L, new RetryPolicy(1))
                .build()
                .getTaskPriority()).isEqualTo(priority);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void buildRejectsNonPositiveTimeout(long timeout) {
        assertThatThrownBy(() -> builder(new PrimeCalculationPayload(10), TaskPriority.NORMAL, timeout, new RetryPolicy(1)).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("timeout must be positive");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t"})
    void buildRejectsBlankIdempotencyKey(String idempotencyKey) {
        assertThatThrownBy(() -> builder(new PrimeCalculationPayload(10), TaskPriority.NORMAL, 5_000L, new RetryPolicy(1))
                .withIdempotencyKey(idempotencyKey)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("idempotencyKey cannot be blank");
    }

    private static TaskBuilder<PrimeCalculationPayload> builder(
            PrimeCalculationPayload payload,
            TaskPriority priority,
            long timeout,
            RetryPolicy retryPolicy) {
        return TaskBuilderFactory.getInstance().createTaskBuilder(
                payload, priority, WorkloadType.CPU_BOUND, timeout, retryPolicy);
    }
}
