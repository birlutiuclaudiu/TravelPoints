package com.disi.travelpoints.dto.energyuser;


import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.enums.TravelUserRole;

public final class UserMapper {

    public static TravelUser clientToEntity(ClientToCreateDTO newClient) {
        return TravelUser.builder()
                .email(newClient.getEmail())
                .firstName(newClient.getFirstName())
                .lastName(newClient.getLastName())
                .role(TravelUserRole.TOURIST)
                .build();
    }

    public static ClientInfoDTO clientToDTO(TravelUser client) {
        return ClientInfoDTO.builder()
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .id(client.getId())
                .build();
    }

    public static DeviceOwnerSelectDTO ownerToDTO(TravelUser client) {
        return DeviceOwnerSelectDTO.builder()
                .email(client.getEmail())
                .ownerId(client.getId())
                .build();
    }

}
