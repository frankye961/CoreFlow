package io.coreflow.unit;

import io.coreflow.domain.task.RetryPolicy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetryPolicyTest {

    @Test
    public void testNoRetryConfiguration() {
        RetryPolicy retryPolicy = new RetryPolicy(null);
        assertEquals(1, retryPolicy.maximumAttempts());
    }

    @Test
    public void testRetryConfigurationWithThreeAttempts() {
        RetryPolicy retryPolicy = new RetryPolicy(3);
        assertEquals(3, retryPolicy.maximumAttempts());
    }

    @Test
    public void testRetryConfigurationWithZeroAttempts() {
        assertThrows(IllegalArgumentException.class, () ->
                new RetryPolicy(0));

    }

    @Test
    public void testRetryConfigurationWithNegativeAttempt() {
        assertThrows(IllegalArgumentException.class, () ->
                new RetryPolicy(-1));
    }

    @Test
    public void testEqualPolicies() {
        RetryPolicy retryPolicy1 = new RetryPolicy(1);
        RetryPolicy retryPolicy2 = new RetryPolicy(1);
        assertEquals(retryPolicy1, retryPolicy2);
    }

    @Test
    public void testDifferentPolicies() {
        RetryPolicy retryPolicy1 = new RetryPolicy(1);
        RetryPolicy retryPolicy2 = new RetryPolicy(2);
        assertThat(retryPolicy1).isNotEqualTo(retryPolicy2);
    }
}
