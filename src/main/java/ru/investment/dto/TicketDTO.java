package ru.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TicketDTO {
    private String name;

    private String title;

    private String ticket;

    private String[] data;

    private Double cost;
}
