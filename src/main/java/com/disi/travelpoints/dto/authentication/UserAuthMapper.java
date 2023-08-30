package com.disi.travelpoints.dto.authentication;

import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.enums.TravelUserRole;


public final class UserAuthMapper {

    public static TravelUser toEntity(InfoRegisterDTO infoRegisterDTO, TravelUserRole role) {
        return TravelUser.builder()
                .email(infoRegisterDTO.getEmail())
                .firstName(infoRegisterDTO.getFirstName())
                .lastName(infoRegisterDTO.getLastName())
                .role(role)
                .build();
    }

}
