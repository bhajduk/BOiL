package com.ahbh.transport.Validator;

import com.ahbh.transport.domain.OptimalProductAssortmentInput;

public class OptimalProductAssortmentInputValidator {
    public static boolean valid(OptimalProductAssortmentInput input){
        return !invalidArrayLength(input);
    }

    private static boolean invalidArrayLength(OptimalProductAssortmentInput input){
        return !coefficientsArraysLengthNotEqual(input.productsResourcesMatrix()) &&
                !missingLimits(input) &&
                !missingProfits(input);
    }

    private static boolean coefficientsArraysLengthNotEqual(double[][] productsResourcesMatrix){
        int validLength = productsResourcesMatrix.length;
        for (double[] rows: productsResourcesMatrix) {
            if(rows.length != validLength){
                return true;
            }
        }
        return false;
    }

    private static boolean missingLimits(OptimalProductAssortmentInput input){
        return input.productsResourcesMatrix().length != input.limits().length;
    }

    private static boolean missingProfits(OptimalProductAssortmentInput input){
        return input.productsResourcesMatrix()[0].length != input.profit().length;
    }
}
