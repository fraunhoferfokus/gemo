package de.fhg.fokus.mdc.jsonObjects;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.fhg.fokus.mdc.utils.JsonDateDeserializer;
import de.fhg.fokus.mdc.utils.JsonDateSerializer;

/**
 * For a better understanding about possible field types, that can be
 * de/serialized or ignored. Please use @JsonIgnore to exclude class fields that
 * are not part of your service contract.
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 * @example to get this running, you'll need to include Jackson. Please add the
 *          following dependencies to your pom.xml
 * 
 *          <pre>
 * <code>
 * 		<dependency>
 * 			<groupId>org.codehaus.jackson</groupId>
 * 			<artifactId>jackson-jaxrs</artifactId>
 * 			<version>1.9.2</version>
 * 		</dependency>
 * 		<dependency>
 * 			<groupId>org.codehaus.jackson</groupId>
 * 			<artifactId>jackson-xc</artifactId>
 * 			<version>1.9.2</version>
 * 		</dependency>
 * 		<dependency>
 * 			<groupId>org.codehaus.jackson</groupId>
 * 			<artifactId>jackson-core-asl</artifactId>
 * 			<version>1.9.2</version>
 * 		</dependency>
 * 		<dependency>
 * 			<groupId>org.codehaus.jackson</groupId>
 * 			<artifactId>jackson-mapper-asl</artifactId>
 * 			<version>1.9.2</version>
 * 		</dependency>
 * </code>
 * </pre>
 * 
 */
public class ExampleType {

	/**
	 * An empty constructor is a must have!
	 */
	public ExampleType() {

	}

	/**
	 * system id (pk)
	 */
	@JsonProperty("_id")
	private int id = -1;

	@JsonProperty("aString")
	private String aString = "";

	@JsonProperty("aBoolean")
	private Boolean aBoolean = false;

	@JsonProperty("aFloat")
	private float aFloat = 0f;

	@JsonProperty("aRealFloat")
	private Float aRealFloat = 0f;

	@JsonProperty("aDouble")
	private double aDouble = 0d;

	@JsonProperty("aRealDouble")
	private Double aRealDouble = 0d;

	/**
	 * Date should have a De/Serializer annotation. You can find them under
	 * "lib"
	 */
	@JsonProperty("aDate")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date aDate;

	// -------------------[ more stuff ]----------------------

	/**
	 * class field to ignore during de/serialization (means, that this field is
	 * not part of the service contract)
	 */
	@JsonIgnore
	private ExampleType hiddenExampleType;

	/**
	 * a class field with another output name (here "user", instead of the
	 * internal name "userprofile")
	 */
	@JsonProperty("user")
	private String userprofile = "";

	/**
	 * more string example stuff that is important for internal usage
	 */
	@JsonProperty("dbfieldcamelcase")
	private String dbfieldCamelCase = "";
}
