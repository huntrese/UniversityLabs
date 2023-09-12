import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for(int i=0;i<8;i++){
            pieces.add(new Piece("WHITE","PAWN",i,1));
        }
        pieces.get(3).getPiece();
        pieces.get(3).getCoords();
        pieces.get(3).hasPromotionRights();

        pieces.add(new Piece("BLACK","PAWN",5,7));
        pieces.get(8).promotion();
        pieces.get(8).getPiece();
        pieces.get(8).hasPromotionRights();

    }
}