package com.ahbh.transport.controller;

import com.ahbh.transport.domain.BrokerIssueInput;
import com.ahbh.transport.domain.BrokerIssueOutput;
import com.ahbh.transport.service.BrokerIssueSolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("broker/solve")
public class BrokerIssueController {

    private BrokerIssueSolver solver;

    public BrokerIssueController(BrokerIssueSolver solver){
       this.solver = solver;
    }

    @PostMapping
    public ResponseEntity<BrokerIssueOutput> solveBrokerIssue(@RequestBody BrokerIssueInput input){
        return new ResponseEntity<>(solver.solve(input), HttpStatus.OK);
    }

}
