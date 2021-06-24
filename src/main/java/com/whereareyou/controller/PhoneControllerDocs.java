package com.whereareyou.controller;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.exception.PhoneAlreadyRegisteredException;
import com.whereareyou.exception.PhonerNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages beer stock")
public interface PhoneControllerDocs {

    @ApiOperation(value = "Phone creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success phone creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    PhoneDTO createPhone(PhoneDTO phoneDTO) throws PhoneAlreadyRegisteredException;

    @ApiOperation(value = "Returns phone found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success phone found in the system"),
            @ApiResponse(code = 404, message = "Phone with given name not found.")
    })
    PhoneDTO findByName(@PathVariable String name) throws PhonerNotFoundException;

    @ApiOperation(value = "Returns a list of all phones registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all phones registered in the system"),
    })
    List<PhoneDTO> listPhones();

    @ApiOperation(value = "Delete a phone found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success phone deleted in the system"),
            @ApiResponse(code = 404, message = "phone with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws PhonerNotFoundException;
}
