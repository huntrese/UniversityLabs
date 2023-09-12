class Piece:
    def __init__(self, color, type, x, y):
        self.color = color
        self.type = type
        self.x = x
        self.y = y
        self.can_promote = True if self.type == "PAWN" else False

    def promotion(self):
        if self.y == 7 and self.can_promote:
            self.type = "QUEEN"
            self.can_promote = False

    def get_piece(self):
        print(f"I am a {self.color} {self.type}")

    def get_coords(self):
        print(f"{self.x},{self.y}")

    def has_promotion_rights(self):
        print("yes" if self.can_promote else "no")
