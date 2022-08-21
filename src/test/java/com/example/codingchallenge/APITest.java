package com.example.codingchallenge;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CodingChallengeApplication.class})
@WebAppConfiguration
class APITest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void testExecuteWorkflow() throws Exception {

        List<User> userList = TestUtil.getUserList();
        User lastUser = userList.get(0);
        String accessToken = "XXXX";

        when(userService.getUsersByPageNumber(3)).thenReturn(userList);
        when(userService.getLastUser(userList)).thenReturn(lastUser);
        when(userService.updateUser(accessToken, lastUser)).thenReturn(lastUser);

        this.mockMvc
                .perform(post("/api/v1/execute").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());

        verify(userService).getUserById(5555);
        verify(userService).deleteUser(accessToken, lastUser);
    }

}

