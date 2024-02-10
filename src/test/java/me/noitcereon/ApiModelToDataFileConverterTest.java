package me.noitcereon;

import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiModelToDataFileConverterTest {

    private static ApiModelToDataFileConverter unitUnderTest;

    @BeforeAll
    public static void initializeTest() {
        unitUnderTest = new ApiModelToDataFileConverter();
    }

    @Test
    public void givenModelData_WhenConvertingToCsvFile_ThenFileIsCreated() throws IOException {
        TempModel model = new TempModel();
        model.name = "Jessica";
        model.age = 22;
        List<TempModel> tempList = new ArrayList<>();
        tempList.add(model);

        // Act and Assert (no exceptions)
        unitUnderTest.createCsvFile(tempList, "tempModel.csv");
    }
    @Test
    public void givenModelData_WhenConvertingToJsonFile_ThenFileIsCreated() throws IOException {
        MethodOutcome expectedOutcome = MethodOutcome.SUCCESS;
        TempModel model = new TempModel();
        model.name = "Noitcereon";
        model.age = 26;
        List<TempModel> tempList = new ArrayList<>();
        tempList.add(model);
        // Act
        MethodOutcome actualOutcome = unitUnderTest.createJsonFile(tempList, "tempModel.json");
        // Assert
        Assertions.assertEquals(expectedOutcome, actualOutcome);
    }

    /**
     * Temporary model to test with... Should be replaced with a more realistic scenario.
     */
    class TempModel {
        public String name;
        public int age;
        @Override
        public String toString(){
            return this.name + ";" + this.age;
        }
    }
}