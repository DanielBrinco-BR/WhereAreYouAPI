package com.whereareyou.service;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.entity.Phone;
import com.whereareyou.exception.PhoneAlreadyRegisteredException;
import com.whereareyou.exception.PhonerNotFoundException;
import com.whereareyou.exception.PhoneStockExceededException;
import com.whereareyou.mapper.PhoneMapper;
import com.whereareyou.repository.PhoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper = PhoneMapper.INSTANCE;

    public PhoneDTO createPhone(PhoneDTO phoneDTO) throws PhoneAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(phoneDTO.getNumber());
        Phone phone = phoneMapper.toModel(phoneDTO);
        Phone savedPhone = phoneRepository.save(phone);
        return phoneMapper.toDTO(savedPhone);
    }

    public PhoneDTO findByNumber(String number) throws PhonerNotFoundException {
        Phone foundPhone = phoneRepository.findByNumber(number)
                .orElseThrow(() -> new PhonerNotFoundException(number));
        return phoneMapper.toDTO(foundPhone);
    }

    public List<PhoneDTO> listAll() {
        return phoneRepository.findAll()
                .stream()
                .map(phoneMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws PhonerNotFoundException {
        verifyIfExists(id);
        phoneRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws PhoneAlreadyRegisteredException {
        Optional<Phone> optSavedPhone = phoneRepository.findByNumber(name);
        if (optSavedPhone.isPresent()) {
            throw new PhoneAlreadyRegisteredException(name);
        }
    }

    private Phone verifyIfExists(Long id) throws PhonerNotFoundException {
        return phoneRepository.findById(id)
                .orElseThrow(() -> new PhonerNotFoundException(id));
    }
}
