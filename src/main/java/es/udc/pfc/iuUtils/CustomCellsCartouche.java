package es.udc.pfc.iuUtils;

import es.udc.pfc.hieroglyphs.Cartouche;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

public class CustomCellsCartouche extends ListCell<Cartouche> {

    private ImageView imageView;

    public CustomCellsCartouche() {
        imageView = new ImageView();
    }

    @Override
    protected void updateItem(Cartouche item, boolean empty) {
        super.updateItem(item, empty);

        if ((item == null) || empty) {
            setGraphic(null);
            setText(null);
        } else {
            imageView.setImage(item.getCartoucheImage());
            setGraphic(imageView);
            // setText(item.getMdcCode());
        }

    }
}
