package com.fastshipmentsdev.backend_fastshipments.test;

public class TestMain {
    public static void main(String[] args) {
        String regTMP = "CALABRIA";
        System.out.println(regTMP.length());
        String regioneDest = Character.toString(regTMP.charAt(0)).toUpperCase()+""+regTMP.substring(1).toLowerCase();
        System.out.println(regioneDest);
        System.out.println(regioneDest.length());
    }
}
