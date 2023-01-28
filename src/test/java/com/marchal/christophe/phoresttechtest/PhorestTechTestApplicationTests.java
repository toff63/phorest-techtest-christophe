package com.marchal.christophe.phoresttechtest;

import com.marchal.christophe.phoresttechtest.salon.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.marchal.christophe.phoresttechtest.SalonFixture.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PhorestTechTestApplicationTests {

    public static final String clientBasePath = "/client";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;


    @BeforeEach
    public void deleteAllBeforeTest() {
        clientRepository.deleteAll();
    }

    @Test
    public void shouldReturnRepositoryIndex() throws Exception {

        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
                jsonPath("$._links.client").exists());
    }

    @Test
    public void shouldCreateEntity() throws Exception {
        mockMvc.perform(post(clientBasePath).content(frodoBagginsClient))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("client/")));
    }

    @Test
    public void shouldNotAllowInvalidEntity() throws Exception {
        mockMvc.perform(post(clientBasePath).content("{\"firstName\":\"Invalid client\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("email must not be null")))
                .andExpect(content().string(containsString("lastName must not be null")))
                .andExpect(content().string(containsString("banned must not be null")))
                .andExpect(content().string(containsString("phone must not be null")));
    }

    @Test
    public void shouldRetrieveEntity() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(clientBasePath).content(frodoBagginsClient))
                .andExpect(status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location);

        mockMvc.perform(get(location)).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(frodoFirstName))
                .andExpect(jsonPath("$.lastName").value(bagginsLastName));
    }


    // TODO add test to retrieve clients with most loyal points

    @Test
    public void shouldUpdateEntity() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(clientBasePath).content(frodoBagginsClient))
                .andExpect(status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location);

        mockMvc.perform(put(location).content(bilboBagginsClient))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.firstName").value(bilboFirstName)).andExpect(
                jsonPath("$.lastName").value(bagginsLastName));
    }

    @Test
    public void shouldPartiallyUpdateEntity() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(clientBasePath).content(frodoBagginsClient))
                .andExpect(status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location);

        mockMvc.perform(
                patch(location).content("{\"firstName\": \"Bilbo Jr.\"}")).andExpect(
                status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.firstName").value("Bilbo Jr.")).andExpect(
                jsonPath("$.lastName").value(bagginsLastName));
    }

    @Test
    public void shouldDeleteEntity() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(clientBasePath).content(frodoBagginsClient))
                .andExpect(status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location);
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }
}
