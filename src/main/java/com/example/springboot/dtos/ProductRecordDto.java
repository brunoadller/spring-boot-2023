package com.example.springboot.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//como os dados que serão passados é o nome e um valor
//serão colocados como parâmetro
public record ProductRecordDto(@NotBlank String name, @NotNull BigDecimal value) {

}