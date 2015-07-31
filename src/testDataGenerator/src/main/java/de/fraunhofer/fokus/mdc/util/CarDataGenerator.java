/**
 * 
 */
package de.fraunhofer.fokus.mdc.util;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Thomas Scheel, thomas.scheel@fokus.fraunhofer.de
 * 
 */
public class CarDataGenerator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Properties prop = new Properties();
    public static final String PATH = "generator.car.input.path";
    public static final String FILENAME = "generator.car.input.file";
    public static final String FILEHEADER = "generator.car.output.header";
    public static final String OUTPUT_FILENAME = "generator.car.output.file";
    public static final String CAR_ID_STRING = "generator.car.carid.string";
    public static final String XML_OUTPUT_FILENAME = "generator.car.output.carfile.xml";
    public static final String OUTPUT_PATH = "generator.car.output.path";

    public CarDataGenerator() {
        try {
            prop.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("config.properties"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generatePositionFile() throws IOException {
        String path = prop.getProperty(PATH);
        String fileName = prop.getProperty(FILENAME);

        // File file = new File(path + fileName); //old Java.io style
        String input = null;
        try {
            // input = FileUtils.readFileToString(file, "UTF-8"); //old Java.io
            // style
            input = FileUtil.load(path, fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();
        JsonObject data = (JsonObject) parser.parse(input);

        JsonArray array = data.getAsJsonArray("placemarks");

        // create file

        // String text = "Hello World.";
        // File target = new File("C:/dev/temp/test.txt");
        //
        // FileOutputStream fos = new FileOutputStream(target, false);
        // BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));
        // br.write(text);
        // br.flush();
        // br.close();
        // fos.flush();
        // fos.close();
        String outputFilePath = prop.getProperty(OUTPUT_PATH)
                + prop.getProperty(OUTPUT_FILENAME);
        Path target = Paths.get(outputFilePath);

        try {
            Path file = Files.createFile(target);
        } catch (FileAlreadyExistsException e) {
            // do nothing else
        }

        // write header
        String headerString = prop.getProperty(FILEHEADER) + "\n";
        Files.write(target, headerString.getBytes(),
                StandardOpenOption.TRUNCATE_EXISTING);

        int i = 1;
        String content = "";
        for (Iterator<JsonElement> arrayIter = array.iterator(); arrayIter
                .hasNext();) {
            JsonObject carPosition = arrayIter.next().getAsJsonObject();
            Double lon = carPosition.get("coordinates").getAsJsonArray().get(0)
                    .getAsDouble();
            Double lat = carPosition.get("coordinates").getAsJsonArray().get(1)
                    .getAsDouble();

            // write car positions

            content += prop.getProperty(CAR_ID_STRING)
                    + String.format("%05d", i) + ";" + lon + ";" + lat + "\n";

            log.info("Car eVehicleID= " + prop.getProperty(CAR_ID_STRING)
                    + String.format("%05d", i) + "  long= " + lon + " lat= "
                    + lat);

            i++;
        }
        i--;
        Files.write(target, content.getBytes(), StandardOpenOption.APPEND);
        log.info("successfully created file: " + target.toString() + " with "
                + i + " entries.");

        // create XML File
        XMLFileGenerator xmlGen = new XMLFileGenerator();
        try {
            xmlGen.generateXMLFile(i);
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
