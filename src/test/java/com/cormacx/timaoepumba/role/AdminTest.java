package com.cormacx.timaoepumba.role;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void shouldBeAbleToAccessAdminEndpointWithAdminUser() throws Exception {
        mockMvc.perform(get("/hello/admin")).andExpect(status().isOk());
    }

}
