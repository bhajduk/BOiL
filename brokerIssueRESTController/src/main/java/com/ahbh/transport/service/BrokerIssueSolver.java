package com.ahbh.transport.service;

import com.ahbh.transport.domain.BrokerIssueInput;
import com.ahbh.transport.domain.BrokerIssueOutput;
import com.ahbh.transport.exception.InvalidInput;
import com.ahbh.transport.exception.NoCycleException;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Math.min;

@Service
public class BrokerIssueSolver {
    private double[][] routeIncome;
    private double[] supplyTable;
    private double[] demandTable;
    private int lockedRoute;
    private boolean balanced = true;

    private double[][] transportTable;
    private Double[][] profitabilityTable;

    List<double[][]> stepByStepSolution = new ArrayList<>();

    public BrokerIssueOutput solve(BrokerIssueInput input) {

        validateInput(input);

        this.routeIncome = calculateRouteIncome(input);
        this.supplyTable = input.getSupplyTable();
        this.demandTable = input.getDemandTable();
        this.lockedRoute = input.getLockedRoute();
        createTransportTable();
        BrokerIssueOutput output = new BrokerIssueOutput();

        output.setMaxIncome(solve());
        output.setStepByStepSolution(stepByStepSolution);
        return output;
    }

    private double[][] calculateRouteIncome(BrokerIssueInput input) {
        double[][] transportCostsTable = input.getTransportCostsTable();
        double[][] newRouteIncome = new double[transportCostsTable.length][transportCostsTable[0].length];
        double[] sellingPriceTable = input.getSellingPriceTable();
        double[] purchasePriceTable = input.getPurchasePriceTable();
        for (int i = 0; i < transportCostsTable.length; i++) {
            for (int j = 0; j < transportCostsTable[i].length; j++) {
                newRouteIncome[i][j] = sellingPriceTable[j] - purchasePriceTable[i] - transportCostsTable[i][j];
            }
        }
        return newRouteIncome;
    }

    private void validateInput(BrokerIssueInput input) {
        double[][] transportCostsTable = input.getTransportCostsTable();
        if (transportCostsTable == null ||
                input.getSupplyTable().length != transportCostsTable.length ||
                input.getDemandTable().length != transportCostsTable[0].length ||
                lockedRoute < -1 ||
                input.getLockedRoute() > input.getDemandTable().length - 1) {
            throw new InvalidInput();
        }

    }

    public double solve(double[][] transportCostsTable, double[] supplyTable, double[] demandTable, int lockedRoute) {
        this.routeIncome = transportCostsTable;
        this.supplyTable = supplyTable;
        this.demandTable = demandTable;
        this.lockedRoute = lockedRoute;
        createTransportTable();

        return solve();
    }

    private double solve() {
        findFirstSolution();

        while (true) {
            System.out.println(objectiveFunctionValue());
            int[] suboptimalIndexes = isOptimal();
            stepByStepSolution.add(Arrays.stream(transportTable).map(double[]::clone).toArray(double[][]::new));
            if (suboptimalIndexes[0] == -1) {
                break;
            }
            List<int[]> cycle = createCycle(suboptimalIndexes);
            refactorTransportTable(cycle, suboptimalIndexes);
        }
        return objectiveFunctionValue();
    }

    private void refactorTransportTable(List<int[]> nodes, int[] maxElem) {
        Double maxElemValue = profitabilityTable[maxElem[0]][maxElem[1]];
        List<Double> values = new ArrayList<>();
        int maxElementIndex = -1;

        // adding elements to list to find the smallest element
        for (int i = 0; i < nodes.size(); i++) {
            int[] node = nodes.get(i);

            // excluding value from locked route
            if (lockedRoute != node[1])
                values.add(transportTable[node[0]][node[1]]);

            // getting index of max value
            if (profitabilityTable[node[0]][node[1]] == maxElemValue)
                maxElementIndex = i;
        }

        double numberToShift = values.stream().min((a, b) -> {
            if (a == 0) return 1;
            if (b == 0) return -1;
            if (a == b) return 0;
            return a > b ? 1 : -1;
        }).orElseThrow(() -> new NoCycleException());

        // shifting the value along with the cycle road
        for (int j = 0; j < nodes.size(); j++) {
            int[] node = nodes.get(maxElementIndex % (nodes.size()));
            maxElementIndex++;
            int x = node[0];
            int y = node[1];

            if (j % 2 == 0) {
                transportTable[x][y] += numberToShift;
            } else {
                transportTable[x][y] -= numberToShift;
            }
        }

    }

    private void findFirstSolution() {
        double[] tempSupplyTable = supplyTable.clone();
        double[] tempDemandTable = demandTable.clone();

        // running additional steps if any route is locked
        if (lockedRoute != -1) calculateLockedRouteFirst(tempDemandTable, tempSupplyTable);

        // finding the first solution with northwest apex method
        for (int i = 0; i < supplyTable.length; i++) {
            if (tempSupplyTable[i] > 0) {
                for (int j = 0; j < demandTable.length; j++) {
                    if (tempDemandTable[j] > 0) {
                        double minValue = min(tempSupplyTable[i], tempDemandTable[j]);

                        transportTable[i][j] = minValue;
                        tempDemandTable[j] -= minValue;
                        tempSupplyTable[i] -= minValue;
                    }
                    if (supplyTable[i] == 0) {
                        break;
                    }
                }
            }
        }
    }

    private double objectiveFunctionValue() {
        double value = 0;
        for (int i = 0; i < routeIncome.length; i++) {
            for (int j = 0; j < routeIncome[i].length; j++) {
                value += routeIncome[i][j] * transportTable[i][j];
            }

        }
        return value;
    }

    private void calculateLockedRouteFirst(double[] tempDemandTable, double[] tempSupplyTable) {
        Map<Integer, Double> indexValueMap = new HashMap<>();

        int loopRange = routeIncome.length;
        if (!balanced) loopRange--;

        for (int i = 0; i < loopRange; i++) {
            indexValueMap.put(i, routeIncome[i][lockedRoute]);
        }
        List<Map.Entry<Integer, Double>> indexes = new ArrayList<>(indexValueMap.entrySet());
        indexes.sort(Map.Entry.comparingByValue((a, b) -> a > b ? -1 : 1));

        for (Map.Entry<Integer, Double> e : indexes) {
            double minValue = min(tempSupplyTable[e.getKey()], tempDemandTable[lockedRoute]);
            transportTable[e.getKey()][lockedRoute] = minValue;
            tempDemandTable[lockedRoute] -= minValue;
            tempSupplyTable[e.getKey()] -= minValue;
            if (tempDemandTable[lockedRoute] == 0)
                break;
        }
    }

    private int[] isOptimal() {
        Double[] alpha = new Double[transportTable.length];
        Double[] beta = new Double[transportTable[0].length];
        boolean isNullPresent = true;
        profitabilityTable = new Double[transportTable.length][transportTable[0].length];

        alpha[0] = 0d;
        int counter = 0;

        while (isNullPresent) {
            isNullPresent = false;
            for (int i = 0; i < transportTable.length; i++) {
                for (int j = 0; j < transportTable[0].length; j++) {
                    if (transportTable[i][j] != 0) {
                        if (alpha[i] != null) {
                            beta[j] = (routeIncome[i][j] - alpha[i]);
                            continue;
                        } else if (beta[j] != null) {
                            alpha[i] = routeIncome[i][j] - beta[j];
                            continue;
                        }
                        isNullPresent = true;
                    }
                }
            }
            if (counter++ > 1000) throw new InvalidInput();
        }

        for (int i = 0; i < transportTable.length; i++) {
            for (int j = 0; j < transportTable[0].length; j++) {
                if (transportTable[i][j] == 0) {
                    profitabilityTable[i][j] = routeIncome[i][j] - alpha[i] - beta[j];
                }
            }
        }
        // Zablokowanie trasy przez ustawienie minimalnej wartości w tablicy wskaźników optymalności
        // w miejscu gdzie nie może się zmieniać
        if (lockedRoute != -1) {
            for (int i = 0; i < transportTable.length; i++) {
                if (transportTable[i][lockedRoute] == 0) {
                    profitabilityTable[i][lockedRoute] = Double.MIN_VALUE;
                }
            }
        }

        int[] maxBiggerThanZero = new int[]{-1, -1};
        double maxElem = Double.MIN_VALUE;

        int i = 0;
        for (Double[] od : profitabilityTable) {
            int j = 0;
            for (Double val : od) {
                Optional<Double> optionalDouble = Optional.ofNullable(val);
                if (optionalDouble.isPresent() && optionalDouble.get() > 0 && optionalDouble.get() > maxElem) {
                    maxElem = profitabilityTable[i][j];
                    maxBiggerThanZero[0] = i;
                    maxBiggerThanZero[1] = j;
                }
                j++;
            }
            i++;
        }

        return maxBiggerThanZero;

    }


    private List<int[]> createCycle(int[] maxElem) {
        // finding row and column size
        int rows = profitabilityTable.length;
        int columns = profitabilityTable[0].length;

        Double maxElemValue = profitabilityTable[maxElem[0]][maxElem[1]];
        List<int[]> cycleEdgeList = new ArrayList<>();

        for (int y1 = 0; y1 < rows; y1++) {
            for (int x1 = 0; x1 < columns; x1++) {
                if (profitabilityTable[y1][x1] == null || profitabilityTable[y1][x1] == maxElemValue) {
                    cycleEdgeList.add(new int[]{y1, x1});
                    for (int y2 = y1 + 1; y2 < rows; y2++) {
                        for (int x2 = x1 + 1; x2 < columns; x2++) {
                            if ((profitabilityTable[y1][x2] == null || profitabilityTable[y1][x2] == maxElemValue)
                                    && (profitabilityTable[y2][x1] == null || profitabilityTable[y2][x1] == maxElemValue)
                                    && (profitabilityTable[y2][x2] == null || profitabilityTable[y2][x2] == maxElemValue)) {
                                cycleEdgeList.add(new int[]{y1, x2});
                                cycleEdgeList.add(new int[]{y2, x2});
                                cycleEdgeList.add(new int[]{y2, x1});

                                if (cycleEdgeList.stream().anyMatch(p -> profitabilityTable[p[0]][p[1]] == maxElemValue)) {
                                    return cycleEdgeList;
                                }
                            }
                        }
                    }
                    cycleEdgeList.remove(0);
                }
            }
        }

        return Collections.emptyList();
    }

    private void extendSupplyAndDemandArray() {
        double sumSupply = Arrays.stream(supplyTable).sum();
        double sumDemand = Arrays.stream(demandTable).sum();
        supplyTable = Arrays.copyOf(supplyTable, supplyTable.length + 1);
        demandTable = Arrays.copyOf(demandTable, demandTable.length + 1);
        demandTable[demandTable.length - 1] = sumSupply;
        supplyTable[supplyTable.length - 1] = sumDemand;
    }

    private void createTransportTable() {
        double supply = Arrays.stream(supplyTable).sum();
        double demand = Arrays.stream(demandTable).sum();

        if (supply == demand) {
            transportTable = new double[supplyTable.length][demandTable.length];
        } else {
            balanced = false;
            transportTable = new double[supplyTable.length + 1][demandTable.length + 1];
            extendTransportCostsTable();
            extendSupplyAndDemandArray();
        }
    }

    private void extendTransportCostsTable() {
        routeIncome = Arrays.copyOf(routeIncome, routeIncome.length + 1);
        routeIncome[routeIncome.length - 1] = new double[routeIncome[0].length];
        for (int i = 0; i < routeIncome.length; i++) {
            routeIncome[i] = Arrays.copyOf(routeIncome[i], routeIncome[i].length + 1);
        }
    }

    public static void main(String[] args) {
        BrokerIssueInput input = new BrokerIssueInput();
        input.setSupplyTable(new double[]{1,1,1});
        input.setPurchasePriceTable(new double[]{1,1,1});
        input.setDemandTable(new double[]{1,1});
        input.setSellingPriceTable(new double[]{1,1,1});
        input.setTransportCostsTable(new double[][]{{1,1},{1,1},{1,1}});
        input.setLockedRoute(-1);

        BrokerIssueOutput solve = new BrokerIssueSolver().solve(input);
        System.out.println();
    }
}
