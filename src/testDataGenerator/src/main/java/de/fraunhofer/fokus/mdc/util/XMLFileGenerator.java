/**
 * 
 */
package de.fraunhofer.fokus.mdc.util;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.fokus.mdc.util.jaxbmodel.Data;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.Dataset;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.Float;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.Integer;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.SchemaDsc;
import de.fraunhofer.fokus.mdc.util.jaxbmodel.Text;

/**
 * @author Thomas Scheel, thomas.scheel@fokus.fraunhofer.de
 * 
 */
public class XMLFileGenerator {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	private Properties prop = new Properties();
	private static final String PATH = "generator.car.output.path";
	private static final String XML_OUTPUT_FILENAME = "generator.car.output.carfile.xml";

	public XMLFileGenerator() {
		try {
			prop.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateXMLFile(int i) throws JAXBException {
		// make jaxb magic
		JAXBContext jc = JAXBContext
				.newInstance("de.fraunhofer.fokus.mdc.util.jaxbmodel");
		// Unmarshaller unmarshaller = jc.createUnmarshaller();

		Dataset set = new Dataset();
		set.setSchemaDsc(this.createSchemaDSC());

		// data elemente hinzufügen.....
		this.addData(set.getData(), i);

		// write file
		try {

			File file = new File(prop.getProperty(PATH)
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
		Text text2 = new Text();
		Text text3 = new Text();
		Text text4 = new Text();

		Integer integer1 = new Integer();
		Integer integer2 = new Integer();
		Integer integer3 = new Integer();
		Integer integer4 = new Integer();
		Integer integer5 = new Integer();
		Integer integer6 = new Integer();
		Integer integer7 = new Integer();
		Integer integer8 = new Integer();
		Integer integer9 = new Integer();
		Integer integer10 = new Integer();
		Integer integer11 = new Integer();

		Float float1 = new Float();
		Float float2 = new Float();
		Float float3 = new Float();
		Float float4 = new Float();
		Float float5 = new Float();
		Float float6 = new Float();
		Float float7 = new Float();
		Float float8 = new Float();
		Float float9 = new Float();
		Float float10 = new Float();
		Float float11 = new Float();
		Float float12 = new Float();
		Float float13 = new Float();
		Float float14 = new Float();
		Float float15 = new Float();
		Float float16 = new Float();
		Float float17 = new Float();
		Float float18 = new Float();
		Float float19 = new Float();
		Float float20 = new Float();
		Float float21 = new Float();

		text1.setField("id");
		text2.setField("type");
		integer1.setField("seats");
		integer2.setField("child_seat");
		integer3.setField("wheelchair_spot");
		integer4.setField("navigation");
		integer5.setField("smoking");
		float1.setField("airconditioner");
		float2.setField("recuperation_coefficient");
		float3.setField("gear");
		float4.setField("charging_condition");
		text3.setField("number_plate");
		text4.setField("wifi_code");

		integer6.setField("accompanying_luggage");
		float5.setField("weight");
		integer7.setField("max_speed");
		integer8.setField("engine_performance");
		integer9.setField("range_normal");
		integer10.setField("range_summer");
		integer11.setField("range_winter");
		float6.setField("consumption_normal");
		float7.setField("consumption_sommer");
		float8.setField("consumption_winter");
		float9.setField("battery_capacity");
		float10.setField("charge_time");
		float11.setField("speed_charging");
		float12.setField("load_people");
		float13.setField("load_peopleluggage");
		float14.setField("driving_style_eco");
		float15.setField("driving_style_sport");
		float16.setField("plus_radionavi");
		float17.setField("plus_light");
		float18.setField("plus_seatheater");
		float19.setField("plus_heating");
		float20.setField("plus_aircondmiddle");
		float21.setField("plus_aircondfull");

		List<Object> timestampOrTextOrInteger = root
				.getTimestampOrTextOrInteger();
		timestampOrTextOrInteger.add(text1);
		timestampOrTextOrInteger.add(text2);
		timestampOrTextOrInteger.add(integer1);
		timestampOrTextOrInteger.add(integer2);
		timestampOrTextOrInteger.add(integer3);
		timestampOrTextOrInteger.add(integer4);
		timestampOrTextOrInteger.add(integer5);
		timestampOrTextOrInteger.add(float1);
		timestampOrTextOrInteger.add(float2);
		timestampOrTextOrInteger.add(float3);
		timestampOrTextOrInteger.add(float4);
		timestampOrTextOrInteger.add(text3);
		timestampOrTextOrInteger.add(text4);

		timestampOrTextOrInteger.add(integer6);
		timestampOrTextOrInteger.add(float5);
		timestampOrTextOrInteger.add(integer7);
		timestampOrTextOrInteger.add(integer8);
		timestampOrTextOrInteger.add(integer9);
		timestampOrTextOrInteger.add(integer10);
		timestampOrTextOrInteger.add(integer11);
		timestampOrTextOrInteger.add(float6);
		timestampOrTextOrInteger.add(float7);
		timestampOrTextOrInteger.add(float8);
		timestampOrTextOrInteger.add(float9);
		timestampOrTextOrInteger.add(float10);
		timestampOrTextOrInteger.add(float11);
		timestampOrTextOrInteger.add(float12);
		timestampOrTextOrInteger.add(float13);
		timestampOrTextOrInteger.add(float14);
		timestampOrTextOrInteger.add(float15);
		timestampOrTextOrInteger.add(float16);
		timestampOrTextOrInteger.add(float17);
		timestampOrTextOrInteger.add(float18);
		timestampOrTextOrInteger.add(float19);
		timestampOrTextOrInteger.add(float20);
		timestampOrTextOrInteger.add(float21);

		// root.setTimestampOrTextOrInteger(timestampOrTextOrInteger);

		return root;
	}

	/**
	 * Adds the data.
	 * 
	 * @param dataList
	 *            the data list
	 * @param i
	 *            the i
	 */
	private void addData(List<Data> dataList, int i) {
		// log.info("Car eVehicleID= eVehicle" + String.format("%05d", i) +
		// "  long= " + lon + " lat= " + lat);
		for (int y = 0; y < i; y++) {
			Data carData = generateRandomCar(y);
			dataList.add(carData);
		}
	}

	private Data generateRandomCar(int i) {
		Data car = new Data();
		List<Object> values = car.getTimestampOrTextOrInteger();

		// add id
		Text id = new Text();
		id.setField("id");
		id.setValue(prop.getProperty(CarDataGenerator.CAR_ID_STRING)
				+ String.format("%05d", i + 1));
		values.add(id);

		// type
		Text type = new Text();
		int typeNumber = XMLFileGenerator.randomInt(1, 4);
		type.setField("type");
		switch (typeNumber) {
		case 1:
			type.setValue("compact");
			break;
		case 2:
			type.setValue("scooter");
			break;
		case 3:
			type.setValue("van");
			break;
		}

		values.add(type);

		// seats
		Integer seats = new Integer();
		seats.setField("seats");
		int number = XMLFileGenerator.randomInt(2, 6);
		if (number == 3) {
			number = 4;
		}
		seats.setValue(BigInteger.valueOf(number));
		values.add(seats);

		// child seat
		Integer childSeats = new Integer();
		childSeats.setField("child_seat");
		int csNumber;
		if (number == 2) {
			csNumber = XMLFileGenerator.randomInt(0, 2);
		} else {
			csNumber = XMLFileGenerator.randomInt(0, 3);
		}
		childSeats.setValue(BigInteger.valueOf(csNumber));
		values.add(childSeats);

		// weelchair
		Integer weelchair = new Integer();
		weelchair.setField("wheelchair_spot");
		int wcNumber = XMLFileGenerator.randomInt(0, 2);
		weelchair.setValue(BigInteger.valueOf(wcNumber));
		values.add(weelchair);

		// navi
		Integer navi = new Integer();
		navi.setField("navigation");
		int navNumber = XMLFileGenerator.randomInt(0, 2);
		navi.setValue(BigInteger.valueOf(navNumber));
		values.add(navi);

		// smoking generally not allowed
		Integer smoke = new Integer();
		smoke.setField("smoking");
		smoke.setValue(BigInteger.valueOf(0));
		values.add(smoke);

		// airconditioner in watt?
		Float airconditioner = new Float();
		airconditioner.setField("airconditioner");
		int acValue = randomInt(10, 21) * 100;
		airconditioner.setValue(acValue);
		values.add(airconditioner);

		// recuperation_coefficient 0-1
		Float recuperationCoefficient = new Float();
		recuperationCoefficient.setField("recuperation_coefficient");
		recuperationCoefficient.setValue(randomFloat(0, 1));
		values.add(recuperationCoefficient);

		// gear ??? actually only 0.0 is set
		Float gear = new Float();
		gear.setField("gear");
		gear.setValue(0.0);
		values.add(gear);

		// charging_condition
		Float chargingCondition = new Float();
		chargingCondition.setField("charging_condition");
		float chValue = randomInt(8, 100);
		if (chValue >= 95) {
			chValue = 100;
		}
		chargingCondition.setValue(chValue);
		values.add(chargingCondition);

		// number plate
		Text numberPlate = new Text();
		numberPlate.setField("number_plate");
		numberPlate.setValue("b-gm" + i + 1);
		values.add(numberPlate);

		// wifi
		Text wifi = new Text();
		wifi.setField("wifi_code");
		wifi.setValue(UUID.randomUUID().toString());
		values.add(wifi);

		// accompanying_luggage/ Gepäckmitnahme ja/nein
		Integer accompanying_luggage = new Integer();
		accompanying_luggage.setField("accompanying_luggage");
		accompanying_luggage.setValue(BigInteger.valueOf(XMLFileGenerator
				.randomInt(0, 1)));
		values.add(accompanying_luggage);

		// weight/KFZ-Gewicht
		Float weight = new Float();
		weight.setField("weight");
		weight.setValue(randomFloat(90, 2000));
		values.add(weight);

		// max_speed
		Integer max_speed = new Integer();
		max_speed.setField("max_speed");
		max_speed.setValue(BigInteger.valueOf(XMLFileGenerator.randomInt(45,
				200)));
		values.add(max_speed);
		// engine_performance / Motorleistung in kw
		Integer engine_performance = new Integer();
		engine_performance.setField("engine_performance");
		engine_performance.setValue(BigInteger.valueOf(XMLFileGenerator
				.randomInt(3, 150)));
		values.add(engine_performance);
		// range_normal /Reichweite km
		Integer range_normal = new Integer();
		range_normal.setField("range_normal");
		range_normal.setValue(BigInteger.valueOf(XMLFileGenerator.randomInt(30,
				300)));
		values.add(range_normal);
		// range_summer /Reichweite km
		Integer range_summer = new Integer();
		range_summer.setField("range_summer");
		range_summer.setValue(BigInteger.valueOf(XMLFileGenerator.randomInt(30,
				170)));
		values.add(range_summer);
		// range_winter /Reichweite km
		Integer range_winter = new Integer();
		range_winter.setField("range_winter");
		range_winter.setValue(BigInteger.valueOf(XMLFileGenerator.randomInt(30,
				100)));
		values.add(range_winter);

		// consumption_normal / Normverbrauch [kWh/100km]
		Float consumption_normal = new Float();
		consumption_normal.setField("consumption_normal");
		consumption_normal.setValue(randomFloat(5, 30));
		values.add(consumption_normal);
		// consumption_sommer / Normverbrauch [kWh/100km]
		Float consumption_sommer = new Float();
		consumption_sommer.setField("consumption_sommer");
		consumption_sommer.setValue(randomFloat(5, 30));
		values.add(consumption_sommer);
		// consumption_winter / Normverbrauch [kWh/100km]
		Float consumption_winter = new Float();
		consumption_winter.setField("consumption_winter");
		consumption_winter.setValue(randomFloat(5, 30));
		values.add(consumption_winter);
		// battery_capacity / in kwh
		Float battery_capacity = new Float();
		battery_capacity.setField("battery_capacity");
		battery_capacity.setValue(randomFloat(3, 110));
		values.add(battery_capacity);
		// charge_time / Ladezeit [h] (230V)
		Float charge_time = new Float();
		charge_time.setField("charge_time");
		charge_time.setValue(randomFloat(4, 10));
		values.add(charge_time);
		// speed_charging / Schnellladung [h]
		Float speed_charging = new Float();
		speed_charging.setField("speed_charging");
		speed_charging.setValue(randomFloat(0.5f, 5));
		values.add(speed_charging);
		// load_people / Beladung Fahrer+Beifahrer (150kg)
		Float load_people = new Float();
		load_people.setField("load_people");
		load_people.setValue(randomFloat(1, 4));
		values.add(load_people);
		// load_peopleluggage / Beladung Fahrer+Beifahrer+Gepäck (200kg)
		Float load_peopleluggage = new Float();
		load_peopleluggage.setField("load_peopleluggage");
		load_peopleluggage.setValue(randomFloat(2, 6));
		values.add(load_peopleluggage);
		// driving_style_eco / Fahrstil energiesparend (v-10%)
		Float driving_style_eco = new Float();
		driving_style_eco.setField("driving_style_eco");
		driving_style_eco.setValue(randomFloat(-8, -10));
		values.add(driving_style_eco);
		// driving_style_sport / Fahrstil sportlich (v+10%)
		Float driving_style_sport = new Float();
		driving_style_sport.setField("driving_style_sport");
		driving_style_sport.setValue(randomFloat(8, 10));
		values.add(driving_style_sport);
		// plus_radionavi / Elektr. Zusatzverbraucher + Radio+Navigation (50W)
		Float plus_radionavi = new Float();
		plus_radionavi.setField("plus_radionavi");
		plus_radionavi.setValue(randomFloat(1, 2));
		values.add(plus_radionavi);
		// plus_light / Elektr. Zusatzverbraucher + Abblend-/Schlusslicht (60W)
		Float plus_light = new Float();
		plus_light.setField("plus_light");
		plus_light.setValue(randomFloat(1, 2));
		values.add(plus_light);
		// plus_seatheater / Elektr. Zusatzverbraucher + Sitzheizung (150W)
		Float plus_seatheater = new Float();
		plus_seatheater.setField("plus_seatheater");
		plus_seatheater.setValue(randomFloat(1, 2));
		values.add(plus_seatheater);
		// plus_heating / Elektr. Zusatzverbraucher + Heizung mittel (200W)
		Float plus_heating = new Float();
		plus_heating.setField("plus_heating");
		plus_heating.setValue(randomFloat(1, 3));
		values.add(plus_heating);
		// plus_aircondmiddle / Elektr. Zusatzverbraucher + Klimaanlage mittel
		// (500W)
		Float plus_aircondmiddle = new Float();
		plus_aircondmiddle.setField("plus_aircondmiddle");
		plus_aircondmiddle.setValue(randomFloat(3, 7));
		values.add(plus_aircondmiddle);
		// plus_aircondfull / Elektr. Zusatzverbraucher + Klimaanlage Volllast
		// (2000W)
		Float plus_aircondfull = new Float();
		plus_aircondfull.setField("plus_aircondfull");
		plus_aircondfull.setValue(randomFloat(5, 10));
		values.add(plus_aircondfull);

		return car;
	}

	// <data>
	// <text field="id">ecar_0</text>
	// <text field="type">type</text>
	// <integer field="seats">5</integer>
	// <integer field="child_seat">1</integer>
	// <integer field="wheelchair_spot">1</integer>
	// <integer field="navigation">1</integer>
	// <integer field="smoking">0</integer>
	// <float field="airconditioner">0</float>
	// <float field="recuperation_coefficient">0</float>
	// <float field="gear">0</float>
	// <float field="charging_condition">50</float>
	// <text field="number_plate">B-SHD0421</text>
	// <text field="wifi_code">d7823rfbwe03rbuf37082rbedp0</text>
	// <integer field="accompanying_luggage"/>
	// <float field="weight">0.0</float>
	// <integer field="max_speed"/>
	// <integer field="engine_performance"/>
	// <integer field="range_normal"/>
	// <integer field="range_summer"/>
	// <integer field="range_winter"/>
	// <float field="consumption_normal">0.0</float>
	// <float field="consumption_sommer">0.0</float>
	// <float field="consumption_winter">0.0</float>
	// <float field="battery_capacity">0.0</float>
	// <float field="charge_time">0.0</float>
	// <float field="speed_charging">0.0</float>
	// <float field="load_people">0.0</float>
	// <float field="load_peopleluggage">0.0</float>
	// <float field="driving_style_eco">0.0</float>
	// <float field="driving_style_sport">0.0</float>
	// <float field="plus_radionavi">0.0</float>
	// <float field="plus_light">0.0</float>
	// <float field="plus_seatheater">0.0</float>
	// <float field="plus_heating">0.0</float>
	// <float field="plus_aircondmiddle">0.0</float>
	// <float field="plus_aircondfull">0.0</float>
	// </data>

	private static int randomInt(int low, int high) {
		return (int) (Math.random() * (high - low) + low);
	}

	private static float randomFloat(float low, float high) {
		return (float) (Math.random() * (high - low) + low);
	}
}
