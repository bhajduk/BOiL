package com.ahbh.transport.domain;

import lombok.Data;
import org.apache.commons.math3.optim.linear.Relationship;

@Data
public class LinearConstraintInitialValues {
    private double[] coefficients;
    private Relationship relationship;
    private double value;
}
