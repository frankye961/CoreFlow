package io.coreflow.unit;

import io.coreflow.domain.payloads.AllocatedPressurePayload;
import io.coreflow.domain.payloads.TaskPayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AllocatePressurePayloadTest {

    @Test
    public void testValidPositiveValues() {
        AllocatedPressurePayload payload = new AllocatedPressurePayload(10, 1024);

        assertThat(payload.allocationIterationNumber()).isEqualTo(10);
        assertThat(payload.allocationSize()).isEqualTo(1024);
    }

    @Test
    public void testAllocatedPressurePayloadIsTaskPayload() {
        AllocatedPressurePayload payload = new AllocatedPressurePayload(1, 1);

        assertThat(payload).isInstanceOf(TaskPayload.class);
    }

    @Test
    public void testRejectsZeroIterations() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AllocatedPressurePayload(0, 1024));

        assertThat(exception).hasMessage("allocationIterationNumber must be positive");
    }

    @Test
    public void testRejectsNegativeIterations() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AllocatedPressurePayload(-1, 1024));

        assertThat(exception).hasMessage("allocationIterationNumber must be positive");
    }

    @Test
    public void testRejectsZeroAllocationSize() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AllocatedPressurePayload(10, 0));

        assertThat(exception).hasMessage("allocationSize must be positive");
    }

    @Test
    public void testRejectsNegativeAllocationSize() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AllocatedPressurePayload(10, -1));

        assertThat(exception).hasMessage("allocationSize must be positive");
    }
}
