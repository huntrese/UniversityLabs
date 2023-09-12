from Piece import Piece
class Main:
    def main():
        pieces = []

        for i in range(8):
            pieces.append(Piece("WHITE", "PAWN", i, 1))

        pieces[3].get_piece()
        pieces[3].get_coords()
        pieces[3].has_promotion_rights()

        pieces.append(Piece("BLACK", "PAWN", 5, 7))
        pieces[8].promotion()
        pieces[8].get_piece()
        pieces[8].has_promotion_rights()

if __name__ == "__main__":
    Main.main()
