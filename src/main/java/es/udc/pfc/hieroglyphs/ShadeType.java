package es.udc.pfc.hieroglyphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShadeType {

    public static final ObservableList<ShadeType> shadeTypes = FXCollections
            .observableArrayList(new ShadeType("0"), new ShadeType("1"),
                    new ShadeType("2"), new ShadeType("3"), new ShadeType("4"),
                    new ShadeType("5"), new ShadeType("6"),
                    new ShadeType("7"), new ShadeType("8"), new ShadeType(
                            "9"), new ShadeType("10"), new ShadeType("11"),
                    new ShadeType("12"), new ShadeType("13"), new ShadeType(
                            "14"), new ShadeType("15"));

    private String shadeCode;

    public ShadeType(String shadeCode) {
        this.shadeCode = shadeCode;
    }

    public String getShadeCode() {
        return shadeCode;
    }

    public void setShadeCode(String shadeCode) {
        this.shadeCode = shadeCode;
    }

}
