package com.ahbh.transport.domain;

import lombok.Data;
import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.List;

@Data
public class BrokerIssueInput {
    double[][] transportCostsTable;
    double[] supplyTable;
    double[] demandTable;
    int lockedRoute;
}
