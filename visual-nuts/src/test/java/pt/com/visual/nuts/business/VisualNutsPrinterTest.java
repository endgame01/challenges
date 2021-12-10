package pt.com.visual.nuts.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VisualNutsPrinterTest {

    final VisualNutsPrinter visualNutsPrinter = new VisualNutsPrinter();

    @Test
    void mustReturnNumber() {
        Assertions.assertTrue(visualNutsPrinter.getMessage(2).matches("\\d+"));
    }

    @Test
    void mustReturnVisual() {
        Assertions.assertEquals("Visual", visualNutsPrinter.getMessage(9));
    }

    @Test
    void mustReturnNuts() {
        Assertions.assertEquals("Nuts", visualNutsPrinter.getMessage(25));
    }

    @Test
    void mustReturnVisualNuts() {
        Assertions.assertEquals("Visual Nuts", visualNutsPrinter.getMessage(30));
    }
}
