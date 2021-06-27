package com.whereareyou.controller;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.exception.PhoneNotFoundException;
import com.whereareyou.builder.PhoneDTOBuilder;
import com.whereareyou.service.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.whereareyou.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PhoneControllerTest {

    private static final String PHONE_API_URL_PATH = "/api/v1/phones";
    private static final long INVALID_PHONE_ID = 2l;

    private MockMvc mockMvc;

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private PhoneController phoneController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(phoneController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAPhoneIsCreated() throws Exception {
        // given
        PhoneDTO phoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();

        // when
        when(phoneService.createPhone(phoneDTO)).thenReturn(phoneDTO);

        // then
        mockMvc.perform(post(PHONE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(phoneDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number", is(phoneDTO.getNumber())))
                .andExpect(jsonPath("$.data", is(phoneDTO.getData())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        PhoneDTO phoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();
        phoneDTO.setData(null);

        // then
        mockMvc.perform(post(PHONE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(phoneDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        PhoneDTO phoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();

        //when
        doNothing().when(phoneService).deleteById(phoneDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PHONE_API_URL_PATH + "/" + phoneDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(PhoneNotFoundException.class).when(phoneService).deleteById(INVALID_PHONE_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PHONE_API_URL_PATH + "/" + INVALID_PHONE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
