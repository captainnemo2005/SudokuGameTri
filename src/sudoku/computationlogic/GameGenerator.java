package sudoku.computationlogic;

import sudoku.problemdomain.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;
public class GameGenerator {

    public static int[][] getNewGameGrid(){ return unsolveGame(getSolvedGame());}

    private static int [][] getSolvedGame(){
        Random random = new Random(System.currentTimeMillis());
        int  [][] newGrid = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        for(int value = 1 ; value <= GRID_BOUNDARY ; value++){
            int allocations = 0;
            int interrupt = 0;

            List<Coordinates> alloTracker = new ArrayList<>();


            int attempts = 0;
            while(allocations < GRID_BOUNDARY){
                if(interrupt > 200){
                    alloTracker.forEach(coordinates -> {
                        newGrid[coordinates.getX()][coordinates.getY()] = 0 ;
                    });

                    interrupt = 0;
                    allocations = 0;
                    alloTracker.clear();
                    attempts++;

                    if(attempts > 500){
                        clearArray(newGrid);
                        attempts = 0 ;
                        value = 1;
                    }
                }

                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                if (newGrid[xCoordinate][yCoordinate] == 0) {
                    newGrid[xCoordinate][yCoordinate] = value;
                    if(GameLogic.sudokuIsInvalid(newGrid)){
                        newGrid[xCoordinate][yCoordinate] = 0;
                        interrupt++;
                    }else{
                        alloTracker.add(new Coordinates(xCoordinate,yCoordinate));
                        allocations++;
                    }
                }
            }
        }

        return newGrid;
    }

    private static int [][] unsolveGame(int [][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        boolean solvable = false;

        int[][] solvableArray = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        while (solvable == false) {
            SudokuUtilities.copySudoArrayValues(solvedGame, solvableArray);

            int index = 0;

            while (index < 40) {
                int xCoor = random.nextInt(GRID_BOUNDARY);
                int yCoor = random.nextInt(GRID_BOUNDARY);

                if (solvableArray[xCoor][yCoor] != 0) {
                    solvableArray[xCoor][yCoor] = 0;
                    index++;
                }
            }

            int[][] toBeSolved = new int[GRID_BOUNDARY][GRID_BOUNDARY];

            SudokuUtilities.copySudoArrayValues(solvableArray, toBeSolved);

            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
        }
        return solvableArray;
    }

    private static void clearArray(int[][] newGrid) {
        for(int i = 0 ; i < GRID_BOUNDARY ; i++){
            for(int j = 0 ; j < GRID_BOUNDARY ; j++){
                newGrid[i][j] = 0;
            }
        }

    }
}
