/**
 *
 */
package es.udc.pfc.iuUtils;

import es.udc.pfc.hieroglyphs.Shade;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

/**
 * @author estibaliz.ifranjo
 */
public class CustomCellsShade extends ListCell<Shade> {

    private ImageView imageView;

    public CustomCellsShade() {
        imageView = new ImageView();
    }

    @Override
    protected void updateItem(Shade item, boolean empty) {
        super.updateItem(item, empty);

        if ((item == null) || empty) {
            setGraphic(null);
            setText(null);
        } else {
            imageView.setImage(item.getImageShade());
            setGraphic(imageView);
            // setText(item.getShadeCode());
        }

    }
}
