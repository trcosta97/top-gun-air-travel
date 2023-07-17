package com.topgun.airline.controller;

import com.topgun.airline.domain.Airport;
import com.topgun.airline.domain.flight.Flight;
import com.topgun.airline.domain.flight.FlightDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class FlightControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    JacksonTester<Flight> flightJson;
    @Autowired
    JacksonTester<FlightDTO> flightDTOJson;

    @Test
    @DisplayName("Should return http 400 when requires info are null")
    public void save_1() throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.post("/flight"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should return http 200 when info is valid")
    void save_2() throws Exception {
        String dateString = "2023-11-11";
        LocalDate date = LocalDate.parse(dateString);
        Integer availableSeats = 500;

        var response = mvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/flight")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(flightDTOJson.write(
                                        new FlightDTO(date, Airport.GRU, Airport.BSB, availableSeats)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var expectedJson = flightJson.write(
                new Flight(1L, date, Airport.GRU, Airport.BSB, availableSeats, true)
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }
}