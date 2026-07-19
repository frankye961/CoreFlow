# CoreFlow

CoreFlow is a framework-free Java 21 skeleton for a concurrent task-processing engine. It is intended for deliberate practice with Java core APIs, object-oriented design, generics, records, sealed types, design patterns, concurrency, the Java Memory Model, platform threads, virtual threads, scheduling, backpressure, cancellation, retries, file persistence, JVM profiling, allocation analysis, and garbage-collector comparison.

This repository currently contains architectural scaffolding and implementation guidance only. Methods intentionally throw UnsupportedOperationException or contain TODO documentation.

## Current Status

No engine functionality has been implemented. The code is organized so it compiles as a single-module Maven project while leaving concrete constructors, factories, lifecycle methods, helpers, tests, benchmarks, and concurrency experiments unimplemented.

## Learning Objectives

- Build a Java 21 core application without Spring, Jakarta EE, Lombok, databases, logging frameworks, or utility frameworks.
- Explore platform-thread and virtual-thread execution models.
- Practise safe publication, happens-before reasoning, atomic state transitions, cancellation, retry policies, and backpressure.
- Compare JVM behavior through JFR, heap dumps, allocation measurements, and GC logs.

## Planned Architecture

The planned engine separates submission, scheduling, dispatch, execution, state transitions, event publication, retry decisions, persistence, metrics, CLI entry points, and extension points. The initial Maven module keeps main code dependent only on the Java standard library.

## Planned State Flow

```text
CREATED
  -> VALIDATING
  -> QUEUED or SCHEDULED
  -> DISPATCHED
  -> RUNNING
  -> SUCCEEDED
     FAILED
     RETRY_SCHEDULED
     CANCELLED
     TIMED_OUT
     REJECTED
```

## Planned Package Structure

- `api`: public engine API, handles, submissions, and configuration.
- `domain`: immutable task definitions, payloads, results, context, and snapshots.
- `execution`: handler registry, dispatchers, executors, cancellation, and live executions.
- `execution.decorator`: handler decoration extension points.
- `submission`: Chain of Responsibility extension points for validation and admission control.
- `scheduling`: custom and JDK-backed scheduler skeletons.
- `state`: task statuses and state machine skeleton.
- `retry`: retry policies, backoff strategies, and failure classification.
- `queue`: bounded queue and rejection policy strategies.
- `events`: observer interfaces and event publisher skeletons.
- `persistence`: append-only journal and snapshot-store adapters.
- `metrics`: counters, snapshots, and latency recording.
- `plugin`: ServiceLoader-based handler loading extension point.
- `cli`: future command-line surface.
- `examples`: unimplemented example handlers.

## Planned Design Patterns

Command maps to task definitions and task handlers. Strategy maps to retry, backoff, rejection, listener failure, and failure classification. State maps to the task state machine. Observer maps to event listeners and publishers. Builder maps to engine configuration and task definitions. Factory/Registry maps to handler resolution. Adapter maps to journal and snapshot-store implementations. Chain of Responsibility maps to submission interceptors. Decorator maps to metrics, events, tracing, timeout, and logging wrappers. Each pattern should remain justified by practical complexity; the architecture should not duplicate implementations merely to name a pattern.

## Planned JVM Experiments

Future work will compare allocation patterns, queue contention, state-transition approaches, platform threads, virtual threads, JFR recordings, heap dumps, and GC logs. Documentation and scripts currently describe planned experiments only; they do not claim validation by measurement.

## Planned GC Comparison

G1, ZGC, and Serial GC script skeletons are included. Serial GC is planned as an educational baseline, not a general recommendation.

## Planned Test Strategy

Unit tests will cover state rules, builders, retry policies, queue ordering, handler registration, and failure classification. Integration tests will cover lifecycle, scheduling, retries, persistence recovery, and shutdown. Concurrency tests will use latches, barriers, phasers, repeated execution, deterministic invariants, timeouts, captured worker failures, and race amplification rather than relying only on sleep.

## Planned Benchmark Strategy

JMH skeletons document warmup, measurement, forks, benchmark state scope, dead-code elimination, constant folding, Blackhole usage, coordinated omission, and the difference between microbenchmarks and end-to-end validation.

## Build Instructions

```bash
mvn compile
mvn test-compile
mvn -Pbenchmarks test-compile
mvn -Pjcstress test-compile
```

## Roadmap

1. Fill in validation and immutable construction for domain objects.
2. Implement state-transition rules with atomic publication.
3. Implement bounded queues, scheduler, dispatcher, and cancellation.
4. Add persistence recovery and metrics.
5. Add tests, JMH benchmarks, and JCStress experiments.
6. Run JVM profiling and GC comparison experiments.

## Contribution Guidelines

Keep main code framework-free and standard-library only. Add functionality only with focused tests. Preserve explicit thread-safety documentation. Avoid static mutable registries, service locators, wildcard imports, raw generic types, and global singleton engines.

## Implementation Rules

Until implementation begins intentionally, concrete bodies must remain TODO-only for void methods or throw `UnsupportedOperationException("TODO: not implemented")` for non-void methods and constructors. Do not add placeholder returns such as `return null`, `return false`, `return true`, or `return 0`.
