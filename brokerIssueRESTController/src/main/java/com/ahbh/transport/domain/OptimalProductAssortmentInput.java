package com.ahbh.transport.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.List;

@Data
@Accessors(fluent = true)
public class OptimalProductAssortmentInput {
    double [][] productsResourcesMatrix;
    double [] profit;
    double [] limits;
    List<LinearConstraintInitialValues> additionalConstraints;

    public double[][] getProductsResourcesMatrix() {
        return productsResourcesMatrix;
    }

    public void setProductsResourcesMatrix(double[][] productsResourcesMatrix) {
        this.productsResourcesMatrix = productsResourcesMatrix;
    }

    public double[] getProfit() {
        return profit;
    }

    public void setProfit(double[] profit) {
        this.profit = profit;
    }

    public double[] getLimits() {
        return limits;
    }

    public void setLimits(double[] limits) {
        this.limits = limits;
    }

    public List<LinearConstraintInitialValues> getAdditionalConstraints() {
        return additionalConstraints;
    }

    public void setAdditionalConstraints(List<LinearConstraintInitialValues> additionalConstraints) {
        this.additionalConstraints = additionalConstraints;
    }
}
