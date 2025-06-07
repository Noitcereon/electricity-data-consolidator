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
    @Disabled("Because this feature has not been implemented yet.")
    public void givenModeldata_WhenConvertingToCsvFile_ThenDataWrittenMatchesDataGiven() throws IOException {
        // TODO: Implement Model to CSV conversion and this test. (test part)
        /*
            Use test data (based on real api model) to convert into CSV file.
            Then, make assertions that the CSV file contains the same data.
         */

        // Act
        // unitUnderTest.createCsvFile(testData, "givenModeldata_WhenConvertingToCsvFile_ThenDataWrittenMatchesDataGiven.csv");

        // Assert
        Assertions.fail("Conversion from datamodel to CSV file has not been implemented yet.");
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
        public String toString() {
            return this.name + ";" + this.age;
        }
    }
}