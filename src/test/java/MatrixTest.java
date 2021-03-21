import org.junit.jupiter.api.Test;
import tech.ypsilon.matrix.Main;
import util.Terminal;


public class MatrixTest {

    @Test
    public void test1() {
        //Terminal.loadCommandList("5", "75 575 585", "85 498 69", "85 8544 40567", "", "solve");
        // Terminal.loadCommandList("5", "2 4 3 1" , "1 2 0 1", "3 3 1 2", "", "solve");
        Terminal.loadCommandList("5", "2 2 4 4" , "3 1 2 0", "4 3 1 0", "", "solve");
        Main.main(new String[0]);
    }

    @Test
    public void zeroDimensionalSolution(){
        run("5", "2 4 3 1" , "1 2 0 1", "3 3 1 2", "", "solve");
    }

    @Test
    public void oneDimensionalSolution(){
        run("5", "2 2 4 4" , "3 1 2 0", "4 3 1 0", "", "solve");
    }

    @Test
    public void twoDimensionalSolution(){
        run("5", "1 2 3 0", "2 4 1 0", "4 3 2 0", "", "solve");
    }

    @Test
    public void invertMatrix(){
        run("5", "2 4 1 0" , "3 2 0 1", "", "solve");
    }

    @Test
    public void longMatrix(){
        run("5", "1 2 3 4 0", "2 1 2 3 2", "", "solve");
    }

    private void run(String... commands){
        Terminal.loadCommandList(commands);
        Main.main(new String[0]);
    }

}
