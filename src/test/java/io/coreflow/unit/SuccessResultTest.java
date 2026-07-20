package io.coreflow.unit;

import io.coreflow.domain.hierarchy.Hierarchy;
import io.coreflow.domain.hierarchy.SuccessResult;
import io.coreflow.domain.task.TaskId;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SuccessResultTest {

    @Test
    public void testSuccessResultHappyPath() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        String producedValue = "result";
        OffsetDateTime processedAt = OffsetDateTime.now();
        long executionTime = 10;

        SuccessResult result = new SuccessResult(taskId, producedValue, processedAt, executionTime);

        assertThat(result.task()).isEqualTo(taskId);
        assertThat(result.producedValue()).isEqualTo(producedValue);
        assertThat(result.processedAt()).isEqualTo(processedAt);
        assertThat(result.executionTime()).isEqualTo(executionTime);
    }

    @Test
    public void testSuccessResultIsHierarchy() {
        SuccessResult result = new SuccessResult(
                new TaskId(UUID.randomUUID()),
                "result",
                OffsetDateTime.now(),
                1);

        assertThat(result).isInstanceOf(Hierarchy.class);
    }

    @Test
    public void testRejectsNullProducedValue() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new SuccessResult(new TaskId(UUID.randomUUID()), null, OffsetDateTime.now(), 1));

        assertThat(exception).hasMessage("producedValue is null");
    }

    @Test
    public void testRejectsNegativeExecutionTime() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SuccessResult(new TaskId(UUID.randomUUID()), "result", OffsetDateTime.now(), -1));

        assertThat(exception).hasMessage("executionTime must not be negative");
    }
}
