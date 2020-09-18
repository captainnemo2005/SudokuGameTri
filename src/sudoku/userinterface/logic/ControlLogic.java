package sudoku.userinterface.logic;

import sudoku.computationlogic.GameLogic;
import sudoku.constants.GameState;
import sudoku.constants.Messages;
import sudoku.problemdomain.IStorage;
import sudoku.problemdomain.SudokuGame;
import sudoku.userinterface.IUserInterfaceContract;

import java.io.IOException;

public class ControlLogic implements IUserInterfaceContract.EventListener {

    private IStorage storage;
    private IUserInterfaceContract.View view;

    public ControlLogic(IStorage storage, IUserInterfaceContract.View view) {
        this.storage = storage;
        this.view = view;
    }

    @Override
    public void sonSudokuInput(int x, int y, int input) {
        try{
            SudokuGame gameData = storage.getGameData();
            int[][] newGridState = gameData.getCopyOfGridState();

            newGridState[x][y] = input;
            gameData = new SudokuGame(
                    GameLogic.checkForCompletion(newGridState),
                    newGridState
            );

            storage.updateGameDate(gameData);

            view.updateSquare(x,y,input);

            if(gameData.getGameState() == GameState.COMPLETE){
                view.showDialog(Messages.GAME_COMLETE);
            }
        }catch (IOException e){
            e.printStackTrace();
            view.showError(Messages.ERROR);
        }
    }

    @Override
    public void onDialogClick() {
        try{
            storage.updateGameDate(
                    GameLogic.getNewGame()
            );

            view.updateBoard(storage.getGameData());
        }catch (IOException e){
            e.printStackTrace();
            view.showError(Messages.ERROR);
        }
    }
}
