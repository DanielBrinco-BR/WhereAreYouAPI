package com.whereareyou.mapper;

import com.whereareyou.dto.PhoneDTO;
import com.whereareyou.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PhoneMapper {

    PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);

    Phone toModel(PhoneDTO phoneDTO);

    PhoneDTO toDTO(Phone phone);
}
