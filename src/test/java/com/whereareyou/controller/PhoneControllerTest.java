package com.whereareyou.controller;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.dto.QuantityDTO;
import com.whereareyou.exception.PhonerNotFoundException;
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
    private static final long VALID_PHONE_ID = 1L;
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
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        PhoneDTO phoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();

        //when
        when(phoneService.findByNumber(phoneDTO.getNumber())).thenReturn(phoneDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PHONE_API_URL_PATH + "/" + phoneDTO.getNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(phoneDTO.getNumber())))
                .andExpect(jsonPath("$.data", is(phoneDTO.getData())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        PhoneDTO phoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();

        //when
        when(phoneService.findByNumber(phoneDTO.getNumber())).thenThrow(PhonerNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PHONE_API_URL_PATH + "/" + phoneDTO.getNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithPhonesIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PhoneDTO phoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();

        //when
        when(phoneService.listAll()).thenReturn(Collections.singletonList(phoneDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PHONE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number", is(phoneDTO.getNumber())))
                .andExpect(jsonPath("$[0].data", is(phoneDTO.getData())));
    }

    @Test
    void whenGETListWithoutPhonesIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PhoneDTO phoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();

        //when
        when(phoneService.listAll()).thenReturn(Collections.singletonList(phoneDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PHONE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
        doThrow(PhonerNotFoundException.class).when(phoneService).deleteById(INVALID_PHONE_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PHONE_API_URL_PATH + "/" + INVALID_PHONE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

//    @Test
//    void whenPATCHIsCalledToIncrementGreatherThanMaxThenBadRequestStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(30)
//                .build();
//
//        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(beerService.increment(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);
//
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .con(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
//    }

//    @Test
//    void whenPATCHIsCalledWithInvalidBeerIdToIncrementThenNotFoundStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(30)
//                .build();
//
//        when(beerService.increment(INVALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerNotFoundException.class);
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void whenPATCHIsCalledToDecrementDiscountThenOKstatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(5)
//                .build();
//
//        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(beerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(beerDTO);
//
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
//                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
//                .andExpect(jsonPath("$.type", is(beerDTO.getType().toString())))
//                .andExpect(jsonPath("$.quantity", is(beerDTO.getQuantity())));
//    }
//
//    @Test
//    void whenPATCHIsCalledToDEcrementLowerThanZeroThenBadRequestStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(60)
//                .build();
//
//        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(beerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);
//
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void whenPATCHIsCalledWithInvalidBeerIdToDecrementThenNotFoundStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(5)
//                .build();
//
//        when(beerService.decrement(INVALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerNotFoundException.class);
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO)))
//                .andExpect(status().isNotFound());
//    }
}
