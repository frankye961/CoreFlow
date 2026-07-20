package io.coreflow.unit;

import io.coreflow.domain.payloads.PrimeCalculationPayload;
import io.coreflow.domain.payloads.TaskPayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrimeCalculationPayloadTest {

    @Test
    public void testSmallestValidUpperBoundary() {
        PrimeCalculationPayload payload = new PrimeCalculationPayload(2);

        assertThat(payload.upperBoundary()).isEqualTo(2);
    }

    @Test
    public void testTypicalUpperBoundary() {
        PrimeCalculationPayload payload = new PrimeCalculationPayload(100);

        assertThat(payload.upperBoundary()).isEqualTo(100);
    }

    @Test
    public void testPrimeCalculationPayloadIsTaskPayload() {
        PrimeCalculationPayload payload = new PrimeCalculationPayload(2);

        assertThat(payload).isInstanceOf(TaskPayload.class);
    }

    @Test
    public void testRejectsZeroUpperBoundary() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PrimeCalculationPayload(0));

        assertThat(exception).hasMessage("upperBoundary must be at least 2");
    }

    @Test
    public void testRejectsOneAsUpperBoundary() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PrimeCalculationPayload(1));

        assertThat(exception).hasMessage("upperBoundary must be at least 2");
    }

    @Test
    public void testRejectsNegativeUpperBoundary() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PrimeCalculationPayload(-1));

        assertThat(exception).hasMessage("upperBoundary must be at least 2");
    }
}
