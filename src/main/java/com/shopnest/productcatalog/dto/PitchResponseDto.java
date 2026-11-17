package com.shopnest.productcatalog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PitchResponseDto {
    private UUID id;
    private String message;
}
