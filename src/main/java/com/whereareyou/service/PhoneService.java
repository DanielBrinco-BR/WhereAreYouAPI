package com.whereareyou.service;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.entity.Phone;
import com.whereareyou.exception.PhoneAlreadyRegisteredException;
import com.whereareyou.exception.PhoneNotFoundException;
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
        Phone phone = phoneMapper.toModel(phoneDTO);
        Phone savedPhone = phoneRepository.save(phone);
        return phoneMapper.toDTO(savedPhone);
    }

    public List<PhoneDTO> listAll() {
        return phoneRepository.findAll()
                .stream()
                .map(phoneMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws PhoneNotFoundException {
        verifyIfExists(id);
        phoneRepository.deleteById(id);
    }

    private Phone verifyIfExists(Long id) throws PhoneNotFoundException {
        return phoneRepository.findById(id)
                .orElseThrow(() -> new PhoneNotFoundException(id));
    }
}