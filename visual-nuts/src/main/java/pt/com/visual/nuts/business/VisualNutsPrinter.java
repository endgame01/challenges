package pt.com.visual.nuts.business;

public class VisualNutsPrinter {

    public String getMessage(int i) {
        if (isVisual(i)) {
            if (isNuts(i)) {
                return "Visual Nuts";
            }
            return "Visual";
        } else if (isNuts(i)) {
            return "Nuts";
        }
        return String.valueOf(i);
    }

    private boolean isNuts(int i) {
        return i % 5 == 0;
    }

    private boolean isVisual(int i) {
        return i % 3 == 0;
    }
}
