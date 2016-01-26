package es.udc.pfc.hieroglyphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Cartouche {

    public static final ObservableList<String> cartouchesMDCSamples = FXCollections
            .observableArrayList("<-ra-mn:n-xpr\\R270->",
                    "<1-ra-mn:n-xpr\\R270-1>", "<2-ra-mn:n-xpr\\R270-1>",
                    "<2-ra-mn:n-xpr\\R270-1>", "<0-ra-mn:n-xpr\\R270-1>",
                    "<1-ra-mn:n-xpr\\R270-0>", "<2-ra-mn:n-xpr\\R270-0>",
                    "<0-ra-mn:n-xpr\\R270-2>", "<s-E1:D40-xa:M-R19->",
                    "<s2-E1:D40-xa:M-R19-s1>", "<h1-ra-xa-f-h2>",
                    "<h1-ra-xa-f-h3>", "<h1-ra-xa-f-h1>", "<h1-ra-xa-f-h0>",
                    "<h2-ra-xa-f-h1>", "<h2-ra-xa-f-h0>", "<h3-ra-xa-f-h1>",
                    "<h3-ra-xa-f-h0>", "<h0-ra-xa-f-h2>", "<h0-ra-xa-f-h3>",
                    "<h0-ra-xa-f-h1>", "<h0-ra-xa-f-h0>", "<F-ra-xa-f->");

    private Image cartoucheImage;
    private CartoucheType cartoucheType;

    public CartoucheType getCartoucheType() {
        return cartoucheType;
    }

    public void setCartoucheType(CartoucheType cartoucheType) {
        this.cartoucheType = cartoucheType;
    }

    public Cartouche(CartoucheType cartoucheType, Image cartoucheImage) {
        this.cartoucheType = cartoucheType;
        this.cartoucheImage = cartoucheImage;
    }

    public Image getCartoucheImage() {
        return cartoucheImage;
    }

}