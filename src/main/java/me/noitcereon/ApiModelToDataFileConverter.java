package me.noitcereon;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * A class that handles conversion from Java object to data file, such as a .csv file.<br/>
 * For example:
 * It can take a {@link MeteringPointApiDto} and write its contents into a .csv file.
 *
 * @apiNote This is an initial version that might be abstracted further upon later.
 * If needed, a "DataFileToApiModelConverter" class might be made as well.
 */
public class ApiModelToDataFileConverter {
    private static final Path OUTPUT_DIRECTORY_PATH = Paths.get(System.getProperty("user.dir") + File.separator + "dataFromApi");
    private static final Logger LOG = LoggerFactory.getLogger(ApiModelToDataFileConverter.class);

    public <T> void createCsvFile(List<T> dataModels, String fileName) throws IOException {
        Path filePath = Paths.get(OUTPUT_DIRECTORY_PATH.toString(), fileName);
        if (dataModels == null || dataModels.isEmpty()) {
            LOG.warn("Cannot create .csv file from nothing: dataModels=" + dataModels);
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (T model : dataModels) {
                writer.write(model.toString()); // TODO Implement Model to CSV conversion and this test. toString() is not really a valid option.
            }
            writer.flush();
        }
    }
    public <T> MethodOutcome createJsonFile(List<T> dataModels, String fileName) throws IOException {
        Path filePath = Paths.get(OUTPUT_DIRECTORY_PATH.toString(), fileName);
        if (dataModels == null || dataModels.isEmpty()) {
            LOG.warn("Cannot create .json file from nothing: dataModels=" + dataModels);
            return MethodOutcome.BAD_INPUT_FAILURE;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            ObjectMapper mapper = new ObjectMapper();
            for (T model : dataModels) {
                mapper.writeValue(filePath.toFile(), model);
            }
            writer.flush();
        }
        return MethodOutcome.SUCCESS;
    }
}
