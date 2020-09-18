package sudoku.computationlogic;

import sudoku.problemdomain.SudokuGame;

public class SudokuUtilities {
    public static void copySudoArrayValues(int[][] oldArray, int[][] newArray){
        for(int i = 0 ; i < SudokuGame.GRID_BOUNDARY ; i++){
            for(int j = 0 ; j < SudokuGame.GRID_BOUNDARY ; j++){
                newArray[i][j] = oldArray[i][j];
            }
        }
    }

    public static int[][] copyToNewArray(int[][] oldArray) {
        int[][] newArray = new int[SudokuGame.GRID_BOUNDARY][SudokuGame.GRID_BOUNDARY];

        for (int i = 0; i < SudokuGame.GRID_BOUNDARY; i++) {
            for (int j = 0; j < SudokuGame.GRID_BOUNDARY; j++) {
                newArray[i][j] = oldArray[i][j];
            }
        }

    return newArray;
    }
}
