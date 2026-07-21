package io.coreflow.domain.hierarchy;

public sealed interface Hierarchy permits CancellationResult, FailureResult, SuccessResult, TimeoutResult {
}
