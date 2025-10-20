package org.acme.cache;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/weather")
public class WeatherForecastResource {

    @Inject
    WeatherForecastService service;

    @CacheName("TEST_DATA")
    Cache cache;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @GET
    public WeatherForecast getForecast(@RestQuery String city, @RestQuery long daysInFuture) {
        long executionStart = System.currentTimeMillis();
        List<String> dailyForecasts = Arrays.asList(
                service.getDailyForecast(LocalDate.now().plusDays(daysInFuture), city),
                service.getDailyForecast(LocalDate.now().plusDays(daysInFuture + 1L), city),
                service.getDailyForecast(LocalDate.now().plusDays(daysInFuture + 2L), city));
        long executionEnd = System.currentTimeMillis();
        return new WeatherForecast(dailyForecasts, executionEnd - executionStart);
    }

    @GET
    @Path("foo")
    public Uni<List<String>> test() {
        Log.info("::test::");
        return cache.getAsync("all", key -> {
            Log.info("::cache_miss::");

            return Uni.createFrom().emitter(em -> {
                executor.schedule(() -> {
                    em.complete(List.of("all", "test", UUID.randomUUID().toString()));
                }, 100, TimeUnit.MILLISECONDS); // 
            });
        });
    }

    @PreDestroy
    void destroy() {
        executor.shutdown();
    }

}
