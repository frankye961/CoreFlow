package io.coreflow.domain.payloads;

public record PrimeCalculationPayload(int upperBoundary) implements TaskPayload {

    private final static int LOWER_BOUNDARY = 2;

    public PrimeCalculationPayload(int upperBoundary){
        if(upperBoundary < LOWER_BOUNDARY){
            throw new IllegalArgumentException("upperBoundary must be at least 2");
        }
        this.upperBoundary = upperBoundary;
    }
}
