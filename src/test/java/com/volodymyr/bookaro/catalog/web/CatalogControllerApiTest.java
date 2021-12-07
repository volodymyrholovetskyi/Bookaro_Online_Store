package com.volodymyr.bookaro.catalog.web;

import com.volodymyr.bookaro.catalog.application.port.CatalogUseCase;
import com.volodymyr.bookaro.catalog.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class CatalogControllerApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    CatalogUseCase catalogUseCase;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getAllBooks() {
        // given
        Book effective = new Book("Effective Java", 2005, new BigDecimal("99.00"), 50L);
        Book concurrency = new Book("Java Concurrency", 2006, new BigDecimal("99.00"), 50L);
        when(catalogUseCase.findAll()).thenReturn(List.of(effective, concurrency));
        ParameterizedTypeReference<List<Book>> type = new ParameterizedTypeReference<>() {
        };


        // when
        RequestEntity<Void> request = RequestEntity.get(URI.create("http://localhost:" + port + "/catalog")).build();
        ResponseEntity<List<Book>> response = restTemplate.exchange(request, type);

        // then
        assertEquals(2, response.getBody().size());
    }

}
