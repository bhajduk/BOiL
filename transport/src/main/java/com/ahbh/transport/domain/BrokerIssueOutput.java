package com.ahbh.transport.domain;

import lombok.Data;

import java.util.List;

@Data
public class BrokerIssueOutput {
    List<double[][]> stepByStepSolution;
    double maxIncome;
}
