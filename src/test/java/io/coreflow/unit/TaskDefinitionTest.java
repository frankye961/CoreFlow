package io.coreflow.unit;

import io.coreflow.domain.payloads.PrimeCalculationPayload;
import io.coreflow.domain.task.RetryPolicy;
import io.coreflow.domain.task.TaskDefinition;
import io.coreflow.domain.task.TaskId;
import io.coreflow.domain.task.TaskPriority;
import io.coreflow.domain.task.WorkloadType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskDefinitionTest {

    @Test
    void directBuilderRejectsNonPositiveTimeout() {
        assertThatThrownBy(() -> definitionBuilder().timeout(0).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("timeout must be positive");
    }

    @Test
    void directBuilderRejectsBlankIdempotencyKey() {
        assertThatThrownBy(() -> definitionBuilder().idempotencyKey(" ").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("idempotencyKey cannot be blank");
    }

    private static TaskDefinition.TaskDefinitionBuilder<PrimeCalculationPayload> definitionBuilder() {
        return TaskDefinition.<PrimeCalculationPayload>builder()
                .taskId(new TaskId(UUID.randomUUID()))
                .taskPayload(new PrimeCalculationPayload(10))
                .taskPriority(TaskPriority.NORMAL)
                .workloadType(WorkloadType.CPU_BOUND)
                .scheduledExecutionStartTime(Instant.EPOCH)
                .timeout(1)
                .retryPolicy(new RetryPolicy(1))
                .creationTimestamp(Instant.EPOCH)
                .metadata(Map.of());
    }
}
