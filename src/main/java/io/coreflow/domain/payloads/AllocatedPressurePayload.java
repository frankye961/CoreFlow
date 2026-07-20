package io.coreflow.domain.payloads;

public record AllocatedPressurePayload(int allocationIterationNumber, long allocationSize) implements TaskPayload {

    public AllocatedPressurePayload(int allocationIterationNumber, long allocationSize) {


        if (allocationIterationNumber <= 0) {
            throw new IllegalArgumentException("allocationIterationNumber must be positive");
        }
        if (allocationSize <= 0) {
            throw new IllegalArgumentException("allocationSize must be positive");
        }

        this.allocationIterationNumber = allocationIterationNumber;
        this.allocationSize = allocationSize;
    }
}
