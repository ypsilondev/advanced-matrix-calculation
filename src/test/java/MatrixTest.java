import org.junit.jupiter.api.Test;
import tech.ypsilon.matrix.Main;
import util.Terminal;


public class MatrixTest {

    @Test
    public void test1() {
        //Terminal.loadCommandList("5", "75 575 585", "85 498 69", "85 8544 40567", "", "solve");
        Terminal.loadCommandList("5", "2 4 3 1" , "1 2 4 1", "3 3 1 2", "", "solve");
        Main.main(new String[0]);
    }

}
