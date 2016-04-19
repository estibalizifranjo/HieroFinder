/**
 *
 */
package ifranjo.pfc.hieroglyphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 * @author estibaliz.ifranjo
 */
public class Shade {

    public static final ObservableList<String> shadeSamples = FXCollections
            .observableArrayList("A1#0", "A1#1", "A1#2", "A1#3", "A1#4",
                    "A1#5", "A1#6", "A1#7", "A1#8", "A1#9", "A1#10", "A1#11",
                    "A1#12", "A1#13", "A1#14", "A1#15");

    private Image imageShade;
    private ShadeType shadeType;

    public Shade(ShadeType shadeType, Image imageShade) {
        this.imageShade = imageShade;
        this.shadeType = shadeType;
    }

    public ShadeType getShadeType() {
        return shadeType;
    }

    public void setShadeType(ShadeType shadeType) {
        this.shadeType = shadeType;
    }

    public Image getImageShade() {
        return imageShade;
    }

}
