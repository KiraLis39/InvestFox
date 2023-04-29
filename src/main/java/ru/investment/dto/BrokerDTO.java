package ru.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BrokerDTO {

    private String name;

    @Builder.Default
    private ArrayList<Float> inputList = new ArrayList<>();

    @Builder.Default
    private ArrayList<Float> outputList = new ArrayList<>();
}
