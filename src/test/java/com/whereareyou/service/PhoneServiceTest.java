package com.whereareyou.service;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.entity.Phone;
import com.whereareyou.exception.PhoneAlreadyRegisteredException;
import com.whereareyou.exception.PhonerNotFoundException;
import com.whereareyou.mapper.PhoneMapper;
import com.whereareyou.builder.PhoneDTOBuilder;
import com.whereareyou.exception.PhoneStockExceededException;
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
import static org.hamcrest.Matchers.lessThan;
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

    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private PhoneRepository phoneRepository;

    private PhoneMapper phoneMapper = PhoneMapper.INSTANCE;

    @InjectMocks
    private PhoneService phoneService;

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws PhoneAlreadyRegisteredException {
        // given
        PhoneDTO expectedPhoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();
        Phone expectedSavedPhone = phoneMapper.toModel(expectedPhoneDTO);

        // when
        when(phoneRepository.findByName(expectedPhoneDTO.getNumber())).thenReturn(Optional.empty());
        when(phoneRepository.save(expectedSavedPhone)).thenReturn(expectedSavedPhone);

        //then
        PhoneDTO createdPhoneDTO = phoneService.createPhone(expectedPhoneDTO);

        assertThat(createdPhoneDTO.getId(), is(equalTo(expectedPhoneDTO.getId())));
        assertThat(createdPhoneDTO.getNumber(), is(equalTo(expectedPhoneDTO.getNumber())));
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        // given
        PhoneDTO expectedPhoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();
        Phone duplicatedPhone = phoneMapper.toModel(expectedPhoneDTO);

        // when
        when(phoneRepository.findByName(expectedPhoneDTO.getNumber())).thenReturn(Optional.of(duplicatedPhone));

        // then
        assertThrows(PhoneAlreadyRegisteredException.class, () -> phoneService.createPhone(expectedPhoneDTO));
    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws PhonerNotFoundException {
        // given
        PhoneDTO expectedFoundPhoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();
        Phone expectedFoundPhone = phoneMapper.toModel(expectedFoundPhoneDTO);

        // when
        when(phoneRepository.findByName(expectedFoundPhone.getName())).thenReturn(Optional.of(expectedFoundPhone));

        // then
        PhoneDTO foundPhoneDTO = phoneService.findByNumber(expectedFoundPhoneDTO.getNumber());

        assertThat(foundPhoneDTO, is(equalTo(expectedFoundPhoneDTO)));
    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        // given
        PhoneDTO expectedFoundPhoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();

        // when
        when(phoneRepository.findByName(expectedFoundPhoneDTO.getNumber())).thenReturn(Optional.empty());

        // then
        assertThrows(PhonerNotFoundException.class, () -> phoneService.findByNumber(expectedFoundPhoneDTO.getNumber()));
    }

    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        // given
        PhoneDTO expectedFoundPhoneDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();
        Phone expectedFoundPhone = phoneMapper.toModel(expectedFoundPhoneDTO);

        //when
        when(phoneRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundPhone));

        //then
        List<PhoneDTO> foundListBeersDTO = phoneService.listAll();

        assertThat(foundListBeersDTO, is(not(empty())));
        assertThat(foundListBeersDTO.get(0), is(equalTo(expectedFoundPhoneDTO)));
    }

    @Test
    void whenListBeerIsCalledThenReturnAnEmptyListOfBeers() {
        //when
        when(phoneRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<PhoneDTO> foundListBeersDTO = phoneService.listAll();

        assertThat(foundListBeersDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws PhonerNotFoundException {
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
//
//    @Test
//    void whenDecrementIsCalledThenDecrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
//
//        int quantityToDecrement = 5;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        BeerDTO incrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
//    }
//
//    @Test
//    void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
//
//        int quantityToDecrement = 10;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        BeerDTO incrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(0));
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//    }
//
//    @Test
//    void whenDecrementIsLowerThanZeroThenThrowException() {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//
//        int quantityToDecrement = 80;
//        assertThrows(BeerStockExceededException.class, () -> beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement));
//    }
//
//    @Test
//    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
//        int quantityToDecrement = 10;
//
//        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
//
//        assertThrows(BeerNotFoundException.class, () -> beerService.decrement(INVALID_BEER_ID, quantityToDecrement));
//    }
}
