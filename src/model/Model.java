package model;

import java.util.Random;

public class Model {
    
    /* Adattagok */
    private final int           size;
    private final Player[][]    table;
    private Player              actualPlayer;
    private Player              winner;
    private int                 blackstonesRemains = 0;
    private int                 whitestonesReamins = 0;
    private int                 turns;
    
    /* Konstruktor */
    public Model(int size) {
        this.size =             size;
        this.turns =            5*size;
        this.actualPlayer =     Player.WHITE;
        
        table = new Player[size][size];
        int nobody=0;
        int maxNobodyLimit=0;
        switch (size) {
            case 3:
                maxNobodyLimit = 3;
                break;
            case 4:
                maxNobodyLimit = 8;
                break;
            case 6:
                maxNobodyLimit = 24;
                break;
            default:
                break;
        }
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Player playerStone = genRandStone();
                
                if(null != playerStone) switch (playerStone) {
                    case WHITE:
                        if(whitestonesReamins < size) {
                            whitestonesReamins++;
                            table[i][j] = Player.WHITE;
                        }
                        else if(whitestonesReamins==size && blackstonesRemains<size) {
                            blackstonesRemains++;
                            table[i][j] = Player.BLACK;
                        }
                        else if(whitestonesReamins==size && blackstonesRemains==size) {
                            nobody++;
                            table[i][j] = Player.NOBODY;
                        }   break;
                    case BLACK:
                        if(blackstonesRemains < size) {
                            blackstonesRemains++;
                            table[i][j] = Player.BLACK;
                        }
                        else if(blackstonesRemains == size && whitestonesReamins < size) {
                            whitestonesReamins++;
                            table[i][j] = Player.WHITE;
                        }
                        else if(blackstonesRemains == size && whitestonesReamins == size) {
                            nobody++;
                            table[i][j] = Player.NOBODY;
                        }   break;
                    case NOBODY:
                        if(nobody<maxNobodyLimit) {
                            nobody++;
                            table[i][j] = Player.NOBODY;
                        }
                        else if(nobody==maxNobodyLimit && whitestonesReamins<size) {
                            whitestonesReamins++;
                            table[i][j] = Player.WHITE;
                        }
                        else if(nobody==maxNobodyLimit && blackstonesRemains<size) {
                            blackstonesRemains++;
                            table[i][j] = Player.BLACK;
                        }   break;
                    default:
                        break;                   
                }
            }
        }

    }
    
    /* Program főalgoritmusa, kövek elmozgatása a modellben */ 
    public void step(int clickedRow, int clickedCol, int moveRow, int moveCol) {
        
        if(clickedRow == moveRow+1 && clickedCol == moveCol) { //Felfelé mozgatás történik
            
            changePlayer();
            if(isNextCellNobody(moveRow, moveCol)) {
                moveStoneToNobody(clickedRow, clickedCol, moveRow, moveCol);
                return;
            }
            int originalX = clickedRow;
            int originalY = clickedCol;
            Player clickedStone = table[clickedRow][clickedCol];
            while(moveRow >= 0) {
                Player nextStone = table[moveRow][moveCol];
                table[moveRow][moveCol] = clickedStone;
                clickedStone = nextStone;
                if(isNextCellNobody(moveRow, moveCol)) {
                    table[moveRow][moveCol] = clickedStone;
                    table[originalX][originalY] = Player.NOBODY;
                    return;
                }
                clickedRow--; moveRow--;
            }
            table[originalX][originalY] = Player.NOBODY;
        }
        else if(clickedRow == moveRow-1 && clickedCol == moveCol) { //Lefelé mozgatás történik
            
            changePlayer();
            if(isNextCellNobody(moveRow, moveCol)) {
                moveStoneToNobody(clickedRow, clickedCol, moveRow, moveCol);
                return;
            }
            int originalX = clickedRow;
            int originalY = clickedCol;
            Player clickedStone = table[clickedRow][clickedCol];
            while(moveRow < size) {
                Player nextStone = table[moveRow][moveCol];
                table[moveRow][moveCol] = clickedStone;
                clickedStone = nextStone;
                if(isNextCellNobody(moveRow, moveCol)) {
                    table[moveRow][moveCol] = clickedStone;
                    table[originalX][originalY] = Player.NOBODY;
                    return;
                }
                clickedRow++; moveRow++;
            }
            table[originalX][originalY] = Player.NOBODY;
        }
        else if(clickedRow == moveRow && clickedCol == moveCol+1) { //Balra mozgtás történik
            
            changePlayer();
            if(isNextCellNobody(moveRow, moveCol)) {
                moveStoneToNobody(clickedRow, clickedCol, moveRow, moveCol);
                return;
            }
            int originalX = clickedRow;
            int originalY = clickedCol;
            Player clickedStone = table[clickedRow][clickedCol];
            while(moveCol >= 0) {
                Player nextStone = table[moveRow][moveCol];
                table[moveRow][moveCol] = clickedStone;
                clickedStone = nextStone;
                if(isNextCellNobody(moveRow, moveCol)) {
                    table[moveRow][moveCol] = clickedStone;
                    table[originalX][originalY] = Player.NOBODY;
                    return;
                }
                clickedCol--; moveCol--;
            }
            table[originalX][originalY] = Player.NOBODY;
        }            
        else if(clickedRow == moveRow && clickedCol == moveCol-1) { // Jobbra mozgatás történik
            
            changePlayer();
            if(isNextCellNobody(moveRow, moveCol)) {
                moveStoneToNobody(clickedRow, clickedCol, moveRow, moveCol);
                return;
            }
            int originalX = clickedRow;
            int originalY = clickedCol;
            Player clickedStone = table[clickedRow][clickedCol];
            while(moveCol < size) {
                Player nextStone = table[moveRow][moveCol];
                table[moveRow][moveCol] = clickedStone;
                clickedStone = nextStone;
                if(isNextCellNobody(moveRow, moveCol)) {
                    table[moveRow][moveCol] = clickedStone;
                    table[originalX][originalY] = Player.NOBODY;
                    return;
                }
                clickedCol++; moveCol++;
            }
            table[originalX][originalY] = Player.NOBODY;
        }
        howManyStonesRemained();
    }
    
    
    public boolean isNextCellNobody(int moveRow, int moveCol) {
        return table[moveRow][moveCol] == Player.NOBODY;
    }
    public void moveStoneToNobody(int clickedRow, int clickedCol, int moveRow, int moveCol) {
        table[moveRow][moveCol] = table[clickedRow][clickedCol];
        table[clickedRow][clickedCol] = Player.NOBODY;
    }
    
    public static Player genRandStone() {
        Random rand = new Random();
        int genNum = rand.nextInt(3);
        switch (genNum) {
            case 0:
                return Player.WHITE;
            case 1:
                return Player.BLACK;
            default:
                return Player.NOBODY;
        }
    }
    public void changePlayer() {
        if(actualPlayer == Player.WHITE) {
            actualPlayer = Player.BLACK;
        } else {
            actualPlayer = Player.WHITE;
        }
        turns--;
    }
    public Player getWinner() {
        howManyStonesRemained();
        
        if(whitestonesReamins>blackstonesRemains) {
            winner = Player.WHITE;
        } else if(whitestonesReamins<blackstonesRemains) {
            winner = Player.BLACK;
        } else if(whitestonesReamins==blackstonesRemains) {
            winner = Player.NOBODY;
        }
        return winner;
    }
    public void howManyStonesRemained() {
        whitestonesReamins = 0; blackstonesRemains = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (table[i][j] == Player.WHITE) {
                    whitestonesReamins++;
                } else if(table[i][j] == Player.BLACK) {
                    blackstonesRemains++;
                }
            }
        }
    }
    public Player getButtonContent(int r, int c)    { return table[r][c]; }
    public Player getActualPlayer()                 { return actualPlayer; }
    public int getRemainedTurns()                   { return turns; }
    public int getWhiteStonesNum()                  { return whitestonesReamins; }
    public int getBlackStonesNum()                  { return blackstonesRemains; }
}
