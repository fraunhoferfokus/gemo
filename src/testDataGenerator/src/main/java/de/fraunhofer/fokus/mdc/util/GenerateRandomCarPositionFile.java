/**
 * 
 */
package de.fraunhofer.fokus.mdc.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.fokus.mdc.util.jaxbmodel.Data;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.Dataset;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.Float;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.SchemaDsc;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.Text;

/**
 * @author Thomas Scheel, thomas.scheel@fokus.fraunhofer.de
 * 
 */
public class GenerateRandomCarPositionFile {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Properties prop = new Properties();

    public static final String FILEHEADER = "generator.car.output.header";
    // public static final String OUTPUT_FILENAME =
    // "generator.car.output.positionfile.xml";
    // public static final String CAR_ID_STRING = "generator.car.carid.string";
    public static final String XML_OUTPUT_FILENAME = "generator.car.output.positionfile.xml";

    public static final String OUTPUT_PATH = "generator.car.output.path";

    public static final String ROUTING_SERVICE_URL = "generator.car.routing.url";
    public static final String LAT = "generator.car.point.lat";
    public static final String LON = "generator.car.point.lon";
    public static final String CARID = "generator.car.schema.carid";
    public static final String COUNT = "generator.car.count";
    public static final String RADIUS = "generator.car.radius";

    public GenerateRandomCarPositionFile() throws IOException {
        prop.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config.properties"));

    }

    /**
     * Generate position XML file. takes position from config.properties
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void generatePositionFile() throws IOException {

        Double lat = new Double(prop.getProperty(LAT));
        Double lon = new Double(prop.getProperty(LON));
        int count = new Integer(prop.getProperty(COUNT)).intValue();
        Double radius = new Double(prop.getProperty(RADIUS));

        this.generatePositionFile(lat, lon, count, radius);
    }

    /**
     * Generates a positions XML file. The init version.
     * 
     * @param lat
     *            the lat
     * @param lon
     *            the lon
     * @param count
     *            the count
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void generatePositionFile(Double lat, Double lon, int count,
            Double radius) throws IOException {

        Dataset set = new Dataset();
        set.setSchemaDsc(this.createSchemaDSC());

        ArrayList<HashMap<String, Double>> positionsList = this
                .generateRandomStreetPositions(lat, lon, count, radius);

        // data elemente hinzufügen.....
        this.addData(set.getData(), positionsList);

        // write file
        try {

            File file = new File(prop.getProperty(OUTPUT_PATH)
                    + prop.getProperty(XML_OUTPUT_FILENAME));
            JAXBContext jaxbContext = JAXBContext.newInstance(Dataset.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(set, file);
            jaxbMarshaller.marshal(set, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the schema dsc.
     * 
     * @return the schema dsc
     */
    private SchemaDsc createSchemaDSC() {
        SchemaDsc root = new SchemaDsc();
        Text text1 = new Text();
        Float lat = new Float();
        Float lon = new Float();

        text1.setField(prop.getProperty(CARID));
        lat.setField("lat");
        lon.setField("lon");

        List<Object> timestampOrTextOrInteger = root
                .getTimestampOrTextOrInteger();
        timestampOrTextOrInteger.add(text1);
        timestampOrTextOrInteger.add(lat);
        timestampOrTextOrInteger.add(lon);

        return root;
    }

    /**
     * Adds the data.
     * 
     * @param dataList
     *            the data list
     * @param positionsList
     *            the positions list
     */
    private void addData(List<Data> dataList,
            ArrayList<HashMap<String, Double>> positionsList) {
        // log.info("Car eVehicleID= eVehicle" + String.format("%05d", i) +
        // "  long= " + lon + " lat= " + lat);

        int i = 1;
        for (HashMap<String, Double> point : positionsList) {
            // generates XML item from point
            Data carData = generateXMLData(point, i);
            dataList.add(carData);
            i++;
        }
    }

    /**
     * Generate xml data.
     * 
     * @param point
     *            the point
     * @param i
     *            the i
     * @return the data
     */
    private Data generateXMLData(HashMap<String, Double> point, int i) {
        Data car = new Data();
        List<Object> values = car.getTimestampOrTextOrInteger();

        // add id
        Text id = new Text();
        id.setField("id");
        id.setValue(prop.getProperty(CarDataGenerator.CAR_ID_STRING)
                + String.format("%05d", i));
        values.add(id);

        // add lat
        Float lat = new Float();
        lat.setField("lat");
        lat.setValue(point.get("lat"));
        values.add(lat);

        // add lat
        Float lon = new Float();
        lon.setField("lon");
        lon.setValue(point.get("lon"));
        values.add(lon);

        return car;
    }

    /**
     * Gets the street positions. finds a position to a random position that is
     * on a street not in a tarn. To do this, a routing is done between two
     * points, the first and last routing point are definitely on a street.
     * 
     * @param positions
     *            the positions
     * @return the street positions
     */
    protected ArrayList<HashMap<String, Double>> getStreetPositions(
            ArrayList<HashMap<String, Double>> positions) {
        int size = positions.size();
        int halfSize = (int) size / 2;

        String serviceUrl = prop.getProperty(ROUTING_SERVICE_URL);

        // get routing client
        YOURSRoutingClient routerClient = YOURSRoutingClient
                .getInstance(serviceUrl);

        ArrayList<HashMap<String, Double>> resultList = new ArrayList<HashMap<String, Double>>();
        // instantiate map
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("format", "geojson"); // return format
        // params.put("flat", ""); // start
        // params.put("flon", "");
        // params.put("tlat", ""); // target
        // params.put("tlon", "");
        params.put("v", "motorcar"); // car routing
        // params.put("fast", new Integer(1)); // fastest rout
        params.put("fast", "1"); // fastest rout
        params.put("layer", "mapnik"); // layer
        params.put("lang", "de"); // for german routing instructions
        params.put("instructions", "0"); // routing instruction 0
        // = off, 1 = on
        params.put("geometry", "1"); // returns routing points

        for (int i = 0; i < halfSize; i++) {
            // do request with p1 i and p2 i+halfSize

            params.put("flat", positions.get(i).get("lat").toString()); // start
            params.put("flon", positions.get(i).get("lon").toString());
            params.put("tlat", positions.get(i + halfSize).get("lat")
                    .toString()); // target
            params.put("tlon", positions.get(i + halfSize).get("lon")
                    .toString());

            JsonNode result = routerClient.getYOURSRoute(params);
            // System.out.println("result: " + result);

            // retrieve coordinates from routing request
            HashMap<String, Double> point1 = new HashMap<String, Double>();
            HashMap<String, Double> point2 = new HashMap<String, Double>();
            JsonNode arrNode = result.get("coordinates");
            if (arrNode.isArray()) {
                int coordinatsCount = arrNode.size();
                // long = 13 lat= 52
                point1.put("lon", arrNode.get(0).get(0).asDouble());
                point1.put("lat", arrNode.get(0).get(1).asDouble());

                point2.put("lon", arrNode.get(coordinatsCount - 1).get(0)
                        .asDouble());
                point2.put("lat", arrNode.get(coordinatsCount - 1).get(1)
                        .asDouble());

                resultList.add(point1);
                resultList.add(point2);
            }
        }
        if (size % 2 == 1) {
            // do one request with size-1 and 0 and only take first point
            params.put("flat", positions.get(size - 1).get("lat").toString()); // start
            params.put("flon", positions.get(size - 1).get("lon").toString());
            params.put("tlat", positions.get(0).get("lat").toString()); // target
            params.put("tlon", positions.get(0).get("lon").toString());

            JsonNode result = routerClient.getYOURSRoute(params);
            // System.out.println("result of last: " + result);

            HashMap<String, Double> point1 = new HashMap<String, Double>();
            JsonNode arrNode = result.get("coordinates");
            if (arrNode.isArray()) {
                // long = 13 lat= 52
                point1.put("lon", arrNode.get(0).get(0).asDouble());
                point1.put("lat", arrNode.get(0).get(1).asDouble());

                resultList.add(point1);
            }
        }

        return resultList;
    }

    /**
     * Generate random street positions around the given point.
     * 
     * @param lat
     *            the lat
     * @param lon
     *            the lon
     * @param count
     *            how many cars you want
     * @param radius
     *            the radius around the point to spawn the cars
     * @return the array list
     */
    public ArrayList<HashMap<String, Double>> generateRandomStreetPositions(
            Double lat, Double lon, int count, Double radius) {

        ArrayList<HashMap<String, Double>> StreetPositions = null;

        ArrayList<HashMap<String, Double>> randomPositions = RandomPositionGenerator
                .generatePositions(count, radius, lat, lon);

        StreetPositions = this.getStreetPositions(randomPositions);

        return StreetPositions;
    }
}
