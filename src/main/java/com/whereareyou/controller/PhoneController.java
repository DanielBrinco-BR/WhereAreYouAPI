package com.whereareyou.controller;

import com.whereareyou.exception.PhoneAlreadyRegisteredException;
import com.whereareyou.exception.PhonerNotFoundException;
import lombok.AllArgsConstructor;
import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @GetMapping("/{name}")
    public PhoneDTO findByName(@PathVariable String name) throws PhonerNotFoundException {
        return phoneService.findByNumber(name);
    }

    @GetMapping
    public List<PhoneDTO> listPhones() {
        return phoneService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws PhonerNotFoundException {
        phoneService.deleteById(id);
    }
}
