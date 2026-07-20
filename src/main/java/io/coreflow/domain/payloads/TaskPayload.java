package io.coreflow.domain.payloads;

public sealed interface TaskPayload permits AllocatedPressurePayload, FileHashPayload, PrimeCalculationPayload, SimulatedIOPayload {
}
