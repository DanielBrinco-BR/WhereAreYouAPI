package com.whereareyou.builder;

import com.whereareyou.dto.PhoneDTO;
import lombok.Builder;

@Builder
public class PhoneDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String number = "+5511988887777";

    @Builder.Default
    private String data = "WhereAreYou, -23.9998948, -45.111888948, 20.0, 24-06-2021, 09:50:00, 55.0%, HoneyPot, true, 4G";

    public PhoneDTO toPhoneDTO() {
        return new PhoneDTO(id, number, data);
    }
}
