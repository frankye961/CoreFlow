package io.coreflow.domain.task;

public record RetryPolicy(Integer maximumAttempts) {

    public RetryPolicy(Integer maximumAttempts) {
        if (maximumAttempts == null) {
            maximumAttempts = 1;
        }
        if (maximumAttempts == 0) {
            throw new IllegalArgumentException("Number of attempts cannot be zero");
        }
        if (maximumAttempts < 0) {
            throw new IllegalArgumentException("Number of attempts cannot be negative");
        }
        this.maximumAttempts = maximumAttempts;
    }
}
