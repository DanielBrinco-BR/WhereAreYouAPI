package com.whereareyou.controller;

import com.whereareyou.exception.PhoneAlreadyRegisteredException;
import com.whereareyou.exception.PhoneNotFoundException;
import lombok.AllArgsConstructor;
import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/phones")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PhoneController implements PhoneControllerDocs {

    private final PhoneService phoneService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneDTO createPhone(@RequestBody @Valid PhoneDTO phoneDTO) throws PhoneAlreadyRegisteredException {
        return phoneService.createPhone(phoneDTO);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PhoneDTO> listPhones(@RequestParam(value="number") String number) throws PhoneNotFoundException {
        List<PhoneDTO> phones = phoneService.listAll();
        List<PhoneDTO> result = new ArrayList<>();

        for(PhoneDTO phoneDTO : phones) {
            if(phoneDTO.getNumber().equals(number)) {
                result.add(phoneDTO);
                deleteById(phoneDTO.getId());
            }
        }

        if(result.isEmpty()) {
            throw new PhoneNotFoundException(number);
        }

        return result;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws PhoneNotFoundException {
        phoneService.deleteById(id);
    }
}
