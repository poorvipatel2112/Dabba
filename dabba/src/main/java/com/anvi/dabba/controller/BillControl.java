package com.anvi.dabba.controller;

import com.anvi.dabba.model.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("api/v1/")
public interface BillControl {
    @PostMapping("bill/generateBill")
    ResponseEntity<String > generateBill(@RequestBody Map<String, Object> requestMap);
    @GetMapping("bill")
    ResponseEntity<List<Bill>> geBill();
}
