package com.disi.travelpoints.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResetPasswordDTO {
    private String email;
    private String password;
}
