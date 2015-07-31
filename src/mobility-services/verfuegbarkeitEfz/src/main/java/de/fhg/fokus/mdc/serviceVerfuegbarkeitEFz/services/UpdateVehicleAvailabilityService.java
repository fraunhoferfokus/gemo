package de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.services;

// imports

//import java.util.logging.Level;

/**
 * The class implements the updating of an electric vehicle availability.
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * (Ilke
 * says: Note that vehicle availability is not updated with the efzavailability
 * table, it is determined based on the reservations made by the reservierungefz
 * service, thus this service is not used actually. I commented it out , since otherwise it
 * appears in the WADL which might be misleading)
 */

//@Path("/updateAvailability")
//public class UpdateVehicleAvailabilityService {
//
//	/** The logger of the class. */
//	private static Logger log = Logger
//			.getLogger(UpdateVehicleAvailabilityService.class.getName());
//
//	/**
//	 * This is the POST method for updating the availability status.
//	 * 
//	 * @param efzid
//	 *            the id of the electric vehicle to update.
//	 * @param status
//	 *            the new status.
//	 * @return the String to return over HTTP - "OK" if everything was allright,
//	 *         "FAIL" otherwise.
//	 * @throws IOException
//	 *             an IO exception if any.
//	 */
//	@POST
//	@Produces(MediaType.TEXT_PLAIN)
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public String updateAvailability(@FormDataParam("efzid") String efzid,
//			@FormDataParam("status") String status, @Context HttpHeaders headers)
//			throws IOException {
//
//		// check the input parameters
//		if (efzid == null || efzid.matches("^\\s*$") || status == null
//				|| status.matches("^\\s*$")) {
//			return "FAIL";
//		}
//
//		// check the type of the submitted status
//		status = status.toLowerCase().trim();
//		if (!status.equals("free") && !status.equals("busy")) {
//			return "FAIL";
//		}
//
//		// get an instance of the client
//		VehicleAvailabilityDataStoreClient availabilityClient = VehicleAvailabilityDataStoreClient
//				.getInstance();
//		if (availabilityClient == null) {
//			return "FAIL";
//		}
//		/********* [forward authentication headers] ***********/
//		availabilityClient.setAuthHeaders(headers);
//
//		// invoke the update method
//		String res = availabilityClient.updateElectricVehicleAvailability(
//				efzid, status);
//		if (res.contains("FAIL") || res.matches("^\\s*$")) {
//			return "FAIL";
//		}
//
//		return "OK";
//	}
//
//	@POST
//	@Produces(MediaType.TEXT_PLAIN)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public String updateAvailabilityParams(
//			MultivaluedMap<String, String> params,
//			@FormParam("efzid") String efzid,
//			@FormParam("status") String status, @Context HttpHeaders headers)
//			throws IOException {
//
//		// check the input parameters
//		if (efzid == null || efzid.matches("^\\s*$") || status == null
//				|| status.matches("^\\s*$")) {
//			return "FAIL";
//		}
//
//		// check the type of the submitted status
//		status = status.toLowerCase().trim();
//		if (!status.equals("free") && !status.equals("busy")) {
//			return "FAIL";
//		}
//
//		// get an instance of the client
//		VehicleAvailabilityDataStoreClient availabilityClient = VehicleAvailabilityDataStoreClient
//				.getInstance();
//		if (availabilityClient == null) {
//			return "FAIL";
//		}
//		/********* [forward authentication headers] ***********/
//		availabilityClient.setAuthHeaders(headers);
//
//		// invoke the update method
//		String res = availabilityClient.updateElectricVehicleAvailability(
//				efzid, status);
//		if (res.contains("FAIL") || res.matches("^\\s*$")) {
//			return "FAIL";
//		}
//
//		return "OK";
//	}
// }
