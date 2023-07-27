package com.anvi.dabba.controllerImpl;

import com.anvi.dabba.controller.BillControl;
import com.anvi.dabba.model.Bill;
import com.anvi.dabba.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@CrossOrigin
@RestController
public class BillControlImpl implements BillControl {
    @Autowired
    private BillService billService;
    @Override
    public ResponseEntity<String> generateBill(Map<String, Object> requestMap) {
        try{
            return billService.generateBill(requestMap);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> geBill() {
        try{
            return billService.getBill();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>() , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
