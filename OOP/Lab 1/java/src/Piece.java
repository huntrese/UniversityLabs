import javax.swing.plaf.TreeUI;

public class Piece {
    private String color;
    private String type;
    private int x;
    private int y;
    private boolean canPromote;

    public Piece(String color, String type, int x, int y){
        this.color=color;
        this.type=type;
        this.x=x;
        this.y=y;
        if (this.type=="PAWN"){
            canPromote=true;
        } else canPromote=false;

    }

    public void promotion(){
        if (y==7 && canPromote== true){
            type="QUEEN";
            canPromote=false;
        }
    }
    public void getPiece(){
        System.out.println(String.format("I am a %S %S",color,type));
    }
    public void getCoords(){
        System.out.println(String.format("%d,%d",x,y));
    }

    public void hasPromotionRights(){
        System.out.println((canPromote==true)?"yes":"no");

    }
}
