package com.ahbh.transport.service;

import com.ahbh.transport.Validator.OptimalProductAssortmentInputValidator;
import com.ahbh.transport.domain.LinearConstraintInitialValues;
import com.ahbh.transport.domain.OptimalProductAssortmentInput;
import com.ahbh.transport.domain.OptimalProductAssortmentOutput;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.StreamSupport;

@Service
public class OptimalProductAssortmentSolver {

    public static OptimalProductAssortmentOutput solve(OptimalProductAssortmentInput input) {

        if(!OptimalProductAssortmentInputValidator.valid(input)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(input.profit(), 0);
        Collection<LinearConstraint> constraints = new ArrayList<>();

        double[][] productsResourcesMatrix = input.productsResourcesMatrix();
        for (int i = 0; i < productsResourcesMatrix.length; i++) {
            double[] constrainArray = productsResourcesMatrix[i];
            constraints.add(new LinearConstraint(constrainArray, Relationship.LEQ, input.limits()[i]));
        }
        addAdditionalConstraints(input, constraints);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair optSolution = solver.optimize(new MaxIter(100), objectiveFunction,
                new LinearConstraintSet(constraints), GoalType.MAXIMIZE, new NonNegativeConstraint(true));


        double[] solution = optSolution.getPoint();
        return new OptimalProductAssortmentOutput(solution, overallProfit(input.profit(), solution));
    }

    private static void addAdditionalConstraints(
            OptimalProductAssortmentInput input,
            Collection<LinearConstraint> constraints){
        for (LinearConstraintInitialValues constraintInitialValues: input.additionalConstraints()) {
            constraints.add(new LinearConstraint(
                    constraintInitialValues.getCoefficients(),
                    constraintInitialValues.getRelationship(),
                    constraintInitialValues.getValue()
            ));
        }
    }

    private static double overallProfit(double[] profit, double[] solution){
        double result = 0;
        for (int i = 0; i < profit.length; i++) {
            result += profit[i] * solution[i];
        }
        return result;
    }

    public static void main(String[] args) {
        OptimalProductAssortmentInput input = new OptimalProductAssortmentInput();
        input.profit(new double[]{1800,2400,3000})
                .limits(new double[]{36000, 48000})
                .productsResourcesMatrix(
                        new double[][]{
                                new double[]{5,3,1},
                                new double[]{1,2,4}

                        });

        OptimalProductAssortmentSolver.solve(input);
    }
}
