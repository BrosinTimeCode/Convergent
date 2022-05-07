public class CommandLineInterface implements GameViewInterface {
    public CommandLineInterface(){

    }
    public void displayBoard(Board board) {
        System.out.println(board);
    }
    public void refreshBoard(Board board) {
    displayBoard(board);}
}
