package nl.han.oose.dea.resources;

import nl.han.oose.dea.resources.HealthCheckResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HealthCheckResourceTest {

    private static final String HEALTHY = "Up & Running";

    @Test
    void isHealthy() {
        // Arrange
        var healthCheckResource = new HealthCheckResource();

        // Act
        String healthy = healthCheckResource.healthy();

        // Assert
        Assertions.assertEquals(HEALTHY, healthy);
    }
}
