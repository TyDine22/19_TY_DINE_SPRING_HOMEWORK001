package com.example._19_ty_dine_spring_homework001.controller;

import com.example._19_ty_dine_spring_homework001.model.entity.Ticket;
import com.example._19_ty_dine_spring_homework001.model.entity.TicketStatus;
import com.example._19_ty_dine_spring_homework001.model.request.BulkRequest;
import com.example._19_ty_dine_spring_homework001.model.request.TicketRequest;
import com.example._19_ty_dine_spring_homework001.model.response.APIResponse;
import com.example._19_ty_dine_spring_homework001.model.response.noPayloadResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    List<Ticket> TICKETS_LIST  = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(6l);
    public TicketController () {
        TICKETS_LIST.add(new Ticket(1l, "Dani","2026-03-10", "New York", "Los Angels", 200.5d, true, TicketStatus.COMPLETED, "A5"));
        TICKETS_LIST.add(new Ticket(2L, "Tivea", "2026-03-11", "Chicago", "Miami", 150.0d, true, TicketStatus.BOOKED, "B12"));
        TICKETS_LIST.add(new Ticket(3L, "Ratana", "2026-03-12", "Los Angeles", "San Francisco", 180.75d, false, TicketStatus.CANCELLED, "C7"));
        TICKETS_LIST.add(new Ticket(4L, "Sophea", "2026-03-13", "Houston", "Dallas", 120.0d, true, TicketStatus.BOOKED, "D3"));
        TICKETS_LIST.add(new Ticket(5L, "Dani", "2026-03-14", "Seattle", "Portland", 210.5d, true, TicketStatus.COMPLETED, "E10"));
    }

    @Operation(summary = "Get All tickets")
    @GetMapping
    public ResponseEntity<APIResponse<List<Ticket>>> getAllTickets(){
        APIResponse<List<Ticket>> res = new APIResponse<>(true, "Ticket retrieved successfully", HttpStatus.OK, TICKETS_LIST, LocalDateTime.now());
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get a ticket by ID")
    @GetMapping("/{ticket-id}")
    public ResponseEntity<APIResponse<List<Ticket>>> getTicketById (@PathVariable("ticket-id") Long ticketId) {
        for (Ticket t : TICKETS_LIST) {
            if (t.getTicketId().equals(ticketId)) {
                APIResponse<List<Ticket>> res = new APIResponse<>(true, "Ticket fetched successfully", HttpStatus.OK, List.of(t), LocalDateTime.now());
                return ResponseEntity.ok(res);
            }
        }
        APIResponse<List<Ticket>> res = new APIResponse<>(false, "No tickets found with the given ID.", HttpStatus.NOT_FOUND, List.of(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @Operation(summary = "Search for ticket(s) by passenger name")
    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<Ticket>>> searchByName(@RequestParam String name) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : TICKETS_LIST) {
            if (t.getPassengerName().equals(name)) {
                result.add(t);
            }
        }
        if(!result.isEmpty()) {
            APIResponse<List<Ticket>> res = new APIResponse<>(true, "Ticket fetched successfully", HttpStatus.OK, result, LocalDateTime.now());
            return ResponseEntity.ok(res);
        }else {
            APIResponse<List<Ticket>> res = new APIResponse<>(false, "No tickets found for the given passenger name.", HttpStatus.OK, List.of(), LocalDateTime.now());
            return ResponseEntity.ok(res);
        }
    }

    @Operation(summary = "Filter tickets by status and travel date")
    @GetMapping("/filter")
    public ResponseEntity<APIResponse<List<Ticket>>> filterTicket (@RequestParam TicketStatus tStatus, @RequestParam String date) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : TICKETS_LIST) {
            if (t.getTicketStatus().equals(tStatus) && t.getTravelDate().equals(date)) {
                result.add(t);
            }
        }
        if(!result.isEmpty()) {
            APIResponse<List<Ticket>> res = new APIResponse<>(true, "Ticket filtered successfully", HttpStatus.OK, result, LocalDateTime.now());
            return ResponseEntity.ok(res);
        }else {
            APIResponse<List<Ticket>> res = new APIResponse<>(false, "No tickets found with given filters.", HttpStatus.OK, List.of(), LocalDateTime.now());
            return ResponseEntity.ok(res);
        }
    }

    @Operation(summary= "Create a new ticket")
    @PostMapping
    public ResponseEntity<APIResponse<Ticket>> createTicket(@RequestBody TicketRequest request) {
        Ticket ticket = new Ticket(atomicLong.getAndIncrement(), request.getPassengerName(), request.getTravelDate(), request.getSourceStation(), request.getDestinationStation(), request.getPrice(), request.isPaymentStatus(), request.getTicketStatus(), request.getSeatNumber());
        APIResponse<Ticket> res = new APIResponse<>(true, "Ticket created successfully", HttpStatus.CREATED, ticket, LocalDateTime.now());
        TICKETS_LIST.add(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary= "Create multiple new tickets")
    @PostMapping("/bulk")
    public ResponseEntity<APIResponse<List<Ticket>>> createTickets(@RequestBody List<TicketRequest> requests) {
        List<Ticket> newTickets = new ArrayList<>();
        for (TicketRequest tRequest : requests) {
            Ticket ticket = new Ticket(atomicLong.getAndIncrement(), tRequest.getPassengerName(), tRequest.getTravelDate(), tRequest.getSourceStation(), tRequest.getDestinationStation(), tRequest.getPrice(), tRequest.isPaymentStatus(), tRequest.getTicketStatus(), tRequest.getSeatNumber());
            TICKETS_LIST.add(ticket);
            newTickets.add(ticket);
        }
        APIResponse<List<Ticket>> res = new APIResponse<>(true, "Tickets created successfully", HttpStatus.CREATED, newTickets, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary= "Update a ticket by ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Long ticketId, @RequestBody TicketRequest request) {
        for (Ticket t : TICKETS_LIST) {
            if (t.getTicketId().equals(ticketId)) {
                t.setPassengerName(request.getPassengerName());
                t.setTravelDate(request.getTravelDate());
                t.setSourceStation(request.getSourceStation());
                t.setDestinationStation(request.getDestinationStation());
                t.setPrice(request.getPrice());
                t.setPaymentStatus(request.isPaymentStatus());
                t.setTicketStatus(request.getTicketStatus());
                t.setSeatNumber(request.getSeatNumber());
                APIResponse<Ticket> res = new APIResponse<>(true, "Ticket updated successfully", HttpStatus.OK, t, LocalDateTime.now());
                return ResponseEntity.ok(res);
            }
        }
        noPayloadResponse res = new noPayloadResponse(false, "No tickets found with given ID", HttpStatus.NOT_FOUND,  LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @Operation(summary= "Update payment status of multiple tickets")
    @PutMapping("/bulk")
    public ResponseEntity<?> updateStatus(@RequestBody BulkRequest bRequest) {
        List<Ticket> updateTicket = new ArrayList<>();
        for (Ticket t : TICKETS_LIST) {
            if (bRequest.getTicketId().contains(t.getTicketId())) {
                t.setPaymentStatus(bRequest.isPaymentStatus());
                updateTicket.add(t);
            }
        }
        if (!updateTicket.isEmpty()) {
            APIResponse<List<Ticket>> res = new APIResponse<>(true, "Payment status updated successfully", HttpStatus.OK, updateTicket, LocalDateTime.now());
            return ResponseEntity.ok(res);
        } else {
            noPayloadResponse res = new noPayloadResponse(false, "No tickets were updated", HttpStatus.NOT_FOUND, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    @Operation(summary= "Delete a ticket using ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long ticketId) {
        boolean deleted = TICKETS_LIST.removeIf(t -> t.getTicketId().equals(ticketId));

        noPayloadResponse resT = new noPayloadResponse(true, "Ticket deleted successfully", HttpStatus.OK, LocalDateTime.now());
        noPayloadResponse resF = new noPayloadResponse(false, "Ticket not found", HttpStatus.NOT_FOUND, LocalDateTime.now());
        if (deleted) {
            return ResponseEntity.ok(resT);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resF);
        }
    }







}
