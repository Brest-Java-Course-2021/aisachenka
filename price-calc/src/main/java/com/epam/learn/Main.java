package com.epam.learn;

public class Main {
    public static void main(String[] args) {
        // Distance
        // Price per km
        // Weight
        // Price per kg
        // Result = d * pr1 + w * pr2

        double distance = 10;
        double pricePerDistance = 20.05;
        double weight = 30;
        double pricePerWeight = 30.5;

        double result = distance* pricePerDistance + weight * pricePerWeight;
        System.out.println("Result: " + result);
    }
}
