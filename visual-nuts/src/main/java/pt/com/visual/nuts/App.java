package pt.com.visual.nuts;

import pt.com.visual.nuts.business.VisualNutsPrinter;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello Visual Nuts!"); // Display the string.
        VisualNutsPrinter visualNutsPrinter = new VisualNutsPrinter();
        for (int i = 1; i <= 100; i++) {
            System.out.println(visualNutsPrinter.getMessage(i));
        }
    }
}
