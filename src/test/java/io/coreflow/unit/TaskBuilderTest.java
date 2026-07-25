package io.coreflow.unit;

import io.coreflow.domain.TaskBuilder;
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
    void buildAppliesDefaultsAndDefensivelyCopiesMetadata() {
        Map<String, String> metadata = new HashMap<>(Map.of("source", "original"));
        PrimeCalculationPayload payload = new PrimeCalculationPayload(100);

        Instant beforeBuild = Instant.now();
        TaskDefinition<PrimeCalculationPayload> task = builder(null, payload, TaskPriority.HIGH,
                null, null, new RetryPolicy(3), null, null, metadata, 5_000L).build();
        Instant afterBuild = Instant.now();

        assertThat(task.getTaskId()).isNotNull();
        assertThat(task.getTaskPayload()).isSameAs(payload);
        assertThat(task.getScheduledExecutionStartTime()).isEqualTo(task.getCreationTimestamp());
        assertThat(task.getCreationTimestamp()).isBetween(beforeBuild, afterBuild);
        assertThat(task.getMetadata()).containsExactly(Map.entry("source", "original"));

        metadata.put("source", "changed");

        assertThat(task.getMetadata()).containsExactly(Map.entry("source", "original"));
        assertThatThrownBy(() -> task.getMetadata().put("new", "value"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void buildPreservesSuppliedValues() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        Instant creationTime = Instant.parse("2026-01-01T10:15:30Z");
        Instant scheduledTime = Instant.parse("2026-01-02T10:15:30Z");
        Instant endTime = Instant.parse("2026-01-02T10:16:30Z");
        RetryPolicy retryPolicy = new RetryPolicy(3);
        PrimeCalculationPayload payload = new PrimeCalculationPayload(100);

        TaskDefinition<PrimeCalculationPayload> task = builder(taskId, payload, TaskPriority.HIGH,
                creationTime, scheduledTime, retryPolicy, "request-42", endTime, Map.of("key", "value"), 5_000L).build();

        assertThat(task.getTaskId()).isEqualTo(taskId);
        assertThat(task.getTaskPayload()).isSameAs(payload);
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

    @ParameterizedTest
    @EnumSource(TaskPriority.class)
    void buildPreservesEachPriority(TaskPriority priority) {
        TaskDefinition<PrimeCalculationPayload> task = builder(null, new PrimeCalculationPayload(10), priority,
                null, null, new RetryPolicy(1), null, null, null, 5_000L).build();

        assertThat(task.getTaskPriority()).isEqualTo(priority);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void buildRejectsNonPositiveTimeout(long timeout) {
        assertThatThrownBy(() -> builder(null, new PrimeCalculationPayload(10), TaskPriority.NORMAL,
                null, null, new RetryPolicy(1), null, null, null, timeout).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Timeout must be greater than 0");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t"})
    void buildRejectsBlankIdempotencyKey(String idempotencyKey) {
        assertThatThrownBy(() -> builder(null, new PrimeCalculationPayload(10), TaskPriority.NORMAL,
                null, null, new RetryPolicy(1), idempotencyKey, null, null, 5_000L).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("idempotencyKey cannot be blank");
    }

    private static TaskBuilder<PrimeCalculationPayload> builder(
            TaskId taskId,
            PrimeCalculationPayload payload,
            TaskPriority priority,
            Instant creationTime,
            Instant scheduledTime,
            RetryPolicy retryPolicy,
            String idempotencyKey,
            Instant scheduledEndTime,
            Map<String, String> metadata,
            long timeout) {
        return new TaskBuilder<>(taskId, payload, priority, TaskStatus.SCHEDULED,
                WorkloadType.CPU_BOUND, scheduledTime, scheduledEndTime, timeout, retryPolicy,
                idempotencyKey, creationTime, metadata);
    }
}
