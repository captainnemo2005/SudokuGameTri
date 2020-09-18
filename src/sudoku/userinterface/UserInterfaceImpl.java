package sudoku.userinterface;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sudoku.constants.GameState;
import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;

import java.util.HashMap;
import java.util.Objects;

public class UserInterfaceImpl implements IUserInterfaceContract.View,EventHandler<KeyEvent>{

    private final Stage stage;
    private final Group root;

    private HashMap<Coordinates,SudokuTextField> textFieldCoordinates;

    private IUserInterfaceContract.EventListener listener;

    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    private static final double BOARD_PADDING = 50;
    private static final double BOARD_X_AND_Y = 576;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0,150,136);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(224,242,241);
    private static final String SUDOKU = "Sudoku";

    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initializaUserInterface();
    }

    private void initializaUserInterface(){
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextField(root);
        drawGridLines(root);
        stage.show();

    }

    private void drawTitle(Group root) {
        Text title = new Text(235,690,SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().addAll();
    }

    private void drawGridLines(Group root) {
        int xAndY = 114;
        int index = 0;
        while(index < 8){
            int thickness ;
            if((index == 2) || (index == 5)){
                thickness = 3;
            }else{
                thickness = 2;
            }

            Rectangle verticalLine = getLine(
                    xAndY + 64 * index,
                    BOARD_PADDING,
                    BOARD_X_AND_Y,
                    thickness
            );
            Rectangle horizontalLine = getLine(
                    BOARD_PADDING,
                    xAndY + 64 * index,
                    thickness,
                    BOARD_X_AND_Y
            );
            root.getChildren().addAll(verticalLine,horizontalLine);

            index++;
        }

    }
    private Rectangle getLine(double x,
                              double y,
                              double height,
                              double width){
        Rectangle rectangle = new Rectangle();

        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setHeight(height);
        rectangle.setWidth(width);

        rectangle.setFill(Color.BLACK);
        return rectangle;
    }
    private void drawTextField(Group root) {
        final int xOrigin = 50;
        final int yOrigin = 50;

        final int xAndYDelta = 64;

        for(int xIndex = 0; xIndex < 9 ; xIndex++){
            for(int yIndex= 0; yIndex < 9 ; yIndex++){
                int x = xOrigin + xIndex * xAndYDelta;
                int y = xOrigin + yIndex * xAndYDelta;

                SudokuTextField title = new SudokuTextField(xIndex, yIndex);

                styleSudokuTitle(title,x,y);

                title.setOnKeyPressed(this);

                textFieldCoordinates.put(new Coordinates(xIndex,yIndex),title);

                root.getChildren().add(title);
            }
        }
    }

    private void styleSudokuTitle(SudokuTextField title, int x, int y){
        Font numberFont = new Font(32);

        title.setFont(numberFont);
        title.setAlignment(Pos.CENTER);

        title.setLayoutX(x);
        title.setLayoutY(y);
        title.setPrefHeight(64);
        title.setPrefWidth(64);
        title.setBackground(Background.EMPTY);
    }
    private void drawSudokuBoard(Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);

        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);

        boardBackground.setFill(BOARD_BACKGROUND_COLOR);

        root.getChildren().addAll(boardBackground);
    }

    private void drawBackground(Group root) {
        Scene scene = new Scene(root,WINDOW_X,WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    @Override
    public void handle(KeyEvent keyEvent){
        if(keyEvent.getEventType() == KeyEvent.KEY_PRESSED){
            if(
                    keyEvent.getText().matches("[0-9]")
            ){
                int value = Integer.parseInt(keyEvent.getText());
                handleInput(value, keyEvent.getSource());
            }else if(keyEvent.getCode() == KeyCode.BACK_SPACE){
                handleInput(0, keyEvent.getSource());
            }else{
                ((TextField) keyEvent.getSource()).setText("");
            }
        }
        keyEvent.consume();
    }

    private void handleInput(int value, Object source){
        listener.sonSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                value);
    }
    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {

    }

    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField title = textFieldCoordinates.get(new Coordinates(x,y));

        String value = Integer.toString(
                input
        );

        if(value.equals("0")) value ="";
        title.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game) {
        for(int xIndex = 0 ; xIndex < 9 ; xIndex++){
            for(int yIndex=0 ; yIndex < 9 ; yIndex++){
                TextField title = textFieldCoordinates.get(new Coordinates(xIndex,yIndex));

                String value = Integer.toString(
                        game.getCopyOfGridState()[xIndex][yIndex]
                );
                if(value.equals("0")) value = "";

                title.setText(
                        value
                );
                if(game.getGameState() == GameState.NEW){
                    if(value.equals("")){
                        title.setStyle("-fx-opacity: 1;");
                        title.setDisable(false);
                    }else{
                        title.setStyle("-fx-opacity: 0.8;");
                        title.setDisable(true);
                    }
                }
            }
        }
    }

    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,message, ButtonType.OK);
        dialog.showAndWait();

        if(dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR,message, ButtonType.OK);
        dialog.showAndWait();
    }
}
