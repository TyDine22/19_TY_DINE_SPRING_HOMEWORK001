package com.example._19_ty_dine_spring_homework001.model.request;

import com.example._19_ty_dine_spring_homework001.model.entity.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    private String passengerName;
    @Schema(example = "2026-03-11")
    private String travelDate;
    private String sourceStation;
    private String destinationStation;
    private Double price;
    private boolean paymentStatus;
    private TicketStatus ticketStatus;
    private String seatNumber;
}
