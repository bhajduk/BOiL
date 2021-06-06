package com.ahbh.transport.controller;

import com.ahbh.transport.domain.BrokerIssueInput;
import com.ahbh.transport.domain.BrokerIssueOutput;
import com.ahbh.transport.domain.OptimalProductAssortmentInput;
import com.ahbh.transport.domain.OptimalProductAssortmentOutput;
import com.ahbh.transport.service.BrokerIssueSolver;
import com.ahbh.transport.service.OptimalProductAssortmentSolver;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
public class SolversController {

    @PostMapping("broker/solve")
    public ResponseEntity<BrokerIssueOutput> solveBrokerIssue(@RequestBody BrokerIssueInput input){
        BrokerIssueSolver brokerIssueSolver = new BrokerIssueSolver();
        return new ResponseEntity<>(brokerIssueSolver.solve(input), HttpStatus.OK);
    }

    @PostMapping("assortment/solve")
    public ResponseEntity<OptimalProductAssortmentOutput> solveOptimalProductAssortment(
            @RequestBody OptimalProductAssortmentInput input){
        return new ResponseEntity<>(OptimalProductAssortmentSolver.solve(input), HttpStatus.OK);
    }
    // Example body:

//    {
//        "productsResourcesMatrix":
//    [
//        [5,3,1],
//        [1,2,4]
//    ],
//        "profit":[1800,2400,3000],
//        "limits":[36000, 48000],
//        "additionalConstraints":
//    [
//        {
//            "coefficients":[1,0,0],
//            "relationship":"LEQ",
//                "value":200
//        },
//        {
//            "coefficients":[0,1,0],
//            "relationship":"EQ",
//                "value":120
//        },
//        {
//            "coefficients":[0,0,1],
//            "relationship":"EQ",
//                "value":60
//        }
//    ]
//    }


}
