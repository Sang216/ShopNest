package com.shopnest.productcatalog.dto;

import com.shopnest.productcatalog.model.Choice;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroqPitchResponseDto {
    private List<Choice> choices;
}
