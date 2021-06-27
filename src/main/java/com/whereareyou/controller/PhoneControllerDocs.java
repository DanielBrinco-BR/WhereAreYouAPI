package com.whereareyou.controller;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.exception.PhoneAlreadyRegisteredException;
import com.whereareyou.exception.PhoneNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("Manages phone data")
public interface PhoneControllerDocs {

    @ApiOperation(value = "Phone creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success phone creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    PhoneDTO createPhone(PhoneDTO phoneDTO) throws PhoneAlreadyRegisteredException;

    @ApiOperation(value = "Returns a list of all phones registered in the system filtered by number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all phones registered in the system filtered by number"),
    })
    List<PhoneDTO> listPhones(@RequestParam(value="number") String number) throws PhoneNotFoundException;

    @ApiOperation(value = "Delete a phone found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success phone deleted in the system"),
            @ApiResponse(code = 404, message = "phone with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws PhoneNotFoundException;
}
