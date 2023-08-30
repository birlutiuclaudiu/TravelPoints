package com.disi.travelpoints.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailDTO {

    private String text;
    private String subject;
}
