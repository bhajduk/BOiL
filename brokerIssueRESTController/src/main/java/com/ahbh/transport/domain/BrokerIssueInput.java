package com.ahbh.transport.domain;

import lombok.Data;

@Data
public class BrokerIssueInput {
    double[][] transportCostsTable;
    double[] supplyTable;
    double[] demandTable;
    double[] purchasePriceTable;
    double[] sellingPriceTable;
    int lockedRoute;
}
