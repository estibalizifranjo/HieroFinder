/**
 *
 */
package es.udc.pfc.hieroglyphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author estibaliz.ifranjo
 */
public class CartoucheType {
    public static final ObservableList<CartoucheType> cartoucheTypes = FXCollections
            .observableArrayList(new CartoucheType('c', 1, 2),
                    new CartoucheType('c', 1, 1), new CartoucheType('c', 2, 1),
                    new CartoucheType('c', 2, 1), new CartoucheType('c', 0, 1),
                    new CartoucheType('c', 1, 0), new CartoucheType('c', 2, 0),
                    new CartoucheType('c', 0, 2), new CartoucheType('s', 1, 2),
                    new CartoucheType('s', 2, 1), new CartoucheType('h', 1, 2),
                    new CartoucheType('h', 1, 3), new CartoucheType('h', 1, 1),
                    new CartoucheType('h', 1, 0), new CartoucheType('h', 2, 1),
                    new CartoucheType('h', 2, 0), new CartoucheType('h', 3, 1),
                    new CartoucheType('h', 3, 0), new CartoucheType('h', 0, 2),
                    new CartoucheType('h', 0, 3), new CartoucheType('h', 0, 1),
                    new CartoucheType('h', 0, 0), new CartoucheType('F', 1, 2));


    private char type;
    private int start;
    private int end;

    public CartoucheType(char type, int start, int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
