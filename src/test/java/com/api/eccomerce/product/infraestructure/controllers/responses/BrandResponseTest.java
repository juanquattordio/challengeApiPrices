package com.api.eccomerce.product.infraestructure.controllers.responses;

import static org.junit.jupiter.api.Assertions.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BrandResponseTest {

    private List<ILoggingEvent> logsList;

    @Test
    void shouldReturnValidBrandResponseWhenBrandIdExists() {
        BrandResponse response = new BrandResponse("1");

        assertEquals("1", response.getBrandId());
        assertEquals("Zara", response.getDescription());
    }

    @Test
    void shouldLogWarningAndReturnEmptyResponseWhenIdDoesNotExist() {
        String unknownBrand = "15";
        String log =
                String.format(
                        "Error creating brand response: No brand found for brandId: %s",
                        unknownBrand);
        setupLogger();

        BrandResponse response = new BrandResponse(unknownBrand);

        assertEquals(unknownBrand, response.getBrandId());
        assertNull(response.getDescription());
        assertEquals(log, logsList.get(0).getFormattedMessage());
        assertEquals(Level.WARN, logsList.get(0).getLevel());
    }

    private void setupLogger() {
        Logger logger = (Logger) LoggerFactory.getLogger(BrandResponse.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        logsList = listAppender.list;
    }
}
