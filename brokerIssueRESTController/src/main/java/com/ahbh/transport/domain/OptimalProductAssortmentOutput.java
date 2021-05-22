package com.ahbh.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptimalProductAssortmentOutput {
    double[] results;
    double sum;
}
