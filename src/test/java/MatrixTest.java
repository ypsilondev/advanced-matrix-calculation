import org.junit.jupiter.api.Test;
import tech.ypsilon.matrix.Main;
import util.Terminal;


public class MatrixTest {

    @Test
    public void test1() {
        //Terminal.loadCommandList("5", "75 575 585", "85 498 69", "85 8544 40567", "", "solve");
        // Terminal.loadCommandList("5", "2 4 3 1" , "1 2 0 1", "3 3 1 2", "", "solve");
        //Terminal.loadCommandList("5", "2 2 4 4" , "3 1 2 0", "4 3 1 0", "", "transpose");
        //Main.main(new String[0]);
    }

    @Test
    public void zeroDimensionalSolution(){
        run("5", "2 4 3 | 1" , "1 2 0 | 1", "3 3 1 | 2", "", "solve");
    }

    @Test
    public void twoDimensionalImage(){
        run("5", "1 2 3" , "1 2 0", "1 2 1", "", "image");
    }

    @Test
    public void rankTest(){
        run("5", "1 2 3" , "1 2 0", "1 2 1", "", "rank");
    }

    @Test
    public void kernelTest(){
        run("5", "1 2 3" , "1 2 0", "1 2 1", "", "kernel");
    }

    @Test
    public void invertTest(){
        run("5", "1 2 3" , "1 2 0", "1 2 1", "", "invert");
    }

    @Test
    public void matrixTransposeTest(){
        run("5", "1 2 3 0" , "1 2 0 1", "1 2 1 2", "", "transpose");
    }



    @Test
    public void oneDimensionalSolution(){
        run("5", "2 2 4 | 4" , "3 1 2 | 0", "4 3 1 | 0", "", "solve");
    }

    @Test
    public void twoDimensionalSolution(){
        run("5", "1 2 3 | 1", "2 4 1 | 2", "4 3 2 | 4", "", "solve");
    }

    @Test
    public void invertMatrix(){
        run("5", "2 4" , "3 2", "", "invert");
    }

    @Test
    public void longMatrix(){
        run("5", "1 2 3 4 | 0", "2 1 2 3 | 2", "", "solve");
    }

    @Test
    public void traceTest(){
        run("5", "1 2 3", "2 2 1", "1 1 0", "", "trace");
    }

    @Test
    public void uebungsKlausurMatrix(){
        run("5", "4 4 2 0 | 0", "4 3 3 3 | 0", "4 1 0 4 | 0", "0 4 1 3 | 0", "", "solve");
    }

    private void run(String... commands){
        Terminal.loadCommandList(commands);
        Terminal.loadCommandList("quit");
        Main.main(new String[0]);
    }

}
