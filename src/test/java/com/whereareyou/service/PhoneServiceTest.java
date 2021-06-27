package com.whereareyou.service;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.entity.Phone;
import com.whereareyou.exception.PhoneAlreadyRegisteredException;
import com.whereareyou.exception.PhoneNotFoundException;
import com.whereareyou.mapper.PhoneMapper;
import com.whereareyou.builder.PhoneDTOBuilder;
import com.whereareyou.repository.PhoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhoneServiceTest {

    @Mock
    private PhoneRepository phoneRepository;

    private final PhoneMapper phoneMapper = PhoneMapper.INSTANCE;

    @InjectMocks
    private PhoneService phoneService;

    @Test
    void whenListPhoneIsCalledThenReturnAListOfPhones() {
        // given
        PhoneDTO expectedFoundPhoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();
        Phone expectedFoundPhone = phoneMapper.toModel(expectedFoundPhoneDTO);

        //when
        when(phoneRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundPhone));

        //then
        List<PhoneDTO> foundListPhonesDTO = phoneService.listAll();

        assertThat(foundListPhonesDTO, is(not(empty())));
        assertThat(foundListPhonesDTO.get(0), is(equalTo(expectedFoundPhoneDTO)));
    }

    @Test
    void whenListPhoneIsCalledThenReturnAnEmptyListOfPhones() {
        //when
        when(phoneRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<PhoneDTO> foundListBeersDTO = phoneService.listAll();

        assertThat(foundListBeersDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAPhoneShouldBeDeleted() throws PhoneNotFoundException {
        // given
        PhoneDTO expectedDeletedPhoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();
        Phone expectedDeletedPhone = phoneMapper.toModel(expectedDeletedPhoneDTO);

        // when
        when(phoneRepository.findById(expectedDeletedPhoneDTO.getId())).thenReturn(Optional.of(expectedDeletedPhone));
        doNothing().when(phoneRepository).deleteById(expectedDeletedPhoneDTO.getId());

        // then
        phoneService.deleteById(expectedDeletedPhoneDTO.getId());

        verify(phoneRepository, times(1)).findById(expectedDeletedPhoneDTO.getId());
        verify(phoneRepository, times(1)).deleteById(expectedDeletedPhoneDTO.getId());
    }
}
