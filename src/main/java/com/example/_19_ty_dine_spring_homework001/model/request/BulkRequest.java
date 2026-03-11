package com.example._19_ty_dine_spring_homework001.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkRequest {
    private List<Long> ticketId;
    private boolean paymentStatus;
}
