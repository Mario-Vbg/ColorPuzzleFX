package eu.lestard.grid;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static eu.lestard.assertj.javafx.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class GridModelTest {

    public static enum States {
        EMPTY,
        FILLED
    }

    private GridModel<States> model;

    @Before
    public void setup(){
        model = new GridModel<>();
        model.setDefaultState(States.EMPTY);
    }

    @Test
    public void testChangeOfNumberOfRowsAndColumnsTriggersReinitialization(){

        assertThat(model.getCells()).isEmpty();
        assertThat(model.numberOfColumns()).hasValue(0);
        assertThat(model.numberOfRows()).hasValue(0);

        model.setNumberOfColumns(3);

        assertThat(model.getCells()).isEmpty();

        model.setNumberOfRows(1);

        assertThat(model.getCells()).hasSize(3);


        model.setNumberOfRows(2);
        assertThat(model.getCells()).hasSize(6);

        model.setNumberOfColumns(4);

        assertThat(model.getCells()).hasSize(8);
    }

    /**
     * When the grid is made bigger, the existing cells stay the way they are.
     */
    @Test
    public void testWhenGridIsMadeBiggerExistingCellsStayInTheGrid(){
        model.setNumberOfColumns(3);
        model.setNumberOfRows(3);

        Cell<States> cell_2_2 = model.getCell(2, 2);
        cell_2_2.changeState(States.FILLED);


        model.setNumberOfColumns(4);

        assertThat(model.getCells()).contains(cell_2_2);

        cell_2_2 = model.getCell(2,2);

        assertThat(cell_2_2.stateProperty()).hasValue(States.FILLED);
    }

    /**
     * When the grid is made smaller, the existing cells whose coordinates are smaller than the
     * new size are still in the grid with it's state.
     */
    @Test
    public void testWhenGridIsMadeSmallerCellsInsideTheNewBoundsStayInTheGrid(){
        model.setNumberOfColumns(3);
        model.setNumberOfRows(3);

        Cell<States> cell_1_1 = model.getCell(1,1);
        cell_1_1.changeState(States.FILLED);


        model.setNumberOfColumns(2);

        assertThat(model.getCells()).contains(cell_1_1);

        cell_1_1 = model.getCell(1,1);

        assertThat(cell_1_1.stateProperty()).hasValue(States.FILLED);

    }

    /**
     * When the grid is made smaller, the existing cells whose coordinates are outside the new size
     * are removed from the Grid.
     */
    @Test
    public void testWhenGridIsMadeSmallerCellsOutOfBoundAreRemovedFromTheGrid(){
        model.setNumberOfColumns(3);
        model.setNumberOfRows(3);

        Cell<States> cell_2_2 = model.getCell(2,2);

        model.setNumberOfColumns(2);

        assertThat(model.getCells()).doesNotContain(cell_2_2);

        cell_2_2 = model.getCell(2,2);

        assertThat(cell_2_2).isNull();
    }


    @Test
    public void testCellsHaveDefaultValue(){
        model.setNumberOfColumns(3);
        model.setNumberOfRows(5);

        model.cells().forEach(cell ->{
            assertThat(cell.stateProperty()).hasValue(States.EMPTY);
        });
    }

    @Test
    public void testGetCell(){
        model.setNumberOfColumns(3);
        model.setNumberOfRows(5);


        final Cell cell_0_0 = model.getCell(0, 0);

        assertThat(cell_0_0).isNotNull();
        assertThat(cell_0_0.getColumn()).isEqualTo(0);
        assertThat(cell_0_0.getRow()).isEqualTo(0);


        final Cell cell_3_5 = model.getCell(2, 4);
        assertThat(cell_3_5).isNotNull();
        assertThat(cell_3_5.getColumn()).isEqualTo(2);
        assertThat(cell_3_5.getRow()).isEqualTo(4);

        final Cell cellOutOfRange = model.getCell(3, 5);

        assertThat(cellOutOfRange).isNull();
    }


    @Test
    public void testGetNeighbours(){
        model.setNumberOfColumns(4);
        model.setNumberOfRows(4);

        final List<Cell<States>> neighbours = model.getNeighbours(2, 2);

        assertThat(neighbours).hasSize(4);

        assertThat(neighbours).contains(model.getCell(1, 2));
        assertThat(neighbours).contains(model.getCell(3, 2));
        assertThat(neighbours).contains(model.getCell(2, 1));
        assertThat(neighbours).contains(model.getCell(2, 3));
    }

    @Test
    public void testGetNeighboursIsEmptyWhenGridIsEmpty(){
        model.setNumberOfColumns(0);
        model.setNumberOfRows(0);

        final List<Cell<States>> neighbours = model.getNeighbours(0, 0);

        assertThat(neighbours).isNotNull().isEmpty();
    }

    @Test
    public void testGetNeighboursAtEdgeOfGrid(){
        model.setNumberOfColumns(4);
        model.setNumberOfRows(4);

        final List<Cell<States>> neighbours = model.getNeighbours(0,0);

        assertThat(neighbours).hasSize(2);
        assertThat(neighbours).contains(model.getCell(0, 1));
        assertThat(neighbours).contains(model.getCell(1, 0));
    }
}
