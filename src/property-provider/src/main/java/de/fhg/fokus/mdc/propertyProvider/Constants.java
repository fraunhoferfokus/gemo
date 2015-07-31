/**
 * 
 */
package de.fhg.fokus.mdc.propertyProvider;

/**
 * The Interface Constants.
 * 
 * @author tsc
 */
public interface Constants {

	/** The Constant DEFAULT_PROPERTY_PATH. */
	static final String PROVIDER_PROPERTY_FILENAME = "provider.properties";

	/** The Constant DEFAULT_PROPERTY_PATH. */
	static final String DEFAULT_PROPERTY_PATH = "default.property.path";

	/** The Constant DEFAULT_PROPERTY_PATH. */
	static final String TEST_PROPERTY_PATH = "test.property.path";

	// The Services

	static final String CONNECTION_URL = "storage.data.hibernate.connection.url";
	static final String CONNECTION_USERNAME = "storage.data.hibernate.connection.username";
	static final String CONNECTION_PASSWORD = "storage.data.hibernate.connection.password";

	static final String CONNECTION_URL_PRIVATE = "storage.user.hibernate.connection.url";
	static final String CONNECTION_USERNAME_PRIVATE = "storage.user.hibernate.connection.username";
	static final String CONNECTION_PASSWORD_PRIVATE = "storage.user.hibernate.connection.password";

	/** The Constant STORAGE_gemo.uri. */
	static final String STORAGE_URI = "gemo.uri.storage";

	/** The Constant STORAGE_gemo.uri. */
	static final String SERVICE_URI_STORAGE_SEARCH = "gemo.uri.storage.search";

	static final String SERVICE_URI_STORAGE_UPDATE = "gemo.uri.storage.update";

	static final String SERVICE_URI_STORAGE_INSERT = "gemo.uri.storage.insert";

	static final String SERVICE_URI_STORAGE_SEARCH_PARAMETER = "gemo.uri.storage.search.parameter";

	static final String SERVICE_URI_STORAGE_PRIVATE_SEARCH = "gemo.uri.storage.private.search";

	static final String SERVICE_URI_STORAGE_PRIVATE_UPDATE = "gemo.uri.storage.private.update";

	static final String SERVICE_URI_STORAGE_PRIVATE_INSERT = "gemo.uri.storage.private.insert";

	static final String SERVICE_URI_STORAGE_PRIVATE_SEARCH_PARAMETER = "gemo.uri.storage.private.search.parameter";

	/** The Constant SERVICE_URI_GENERAL_EFZ_DATA. */
	static final String SERVICE_URI_GENERAL_EFZ_DATA = "gemo.uri.efz.data";

	static final String SERVICE_URI_GENERAL_EFZ_DATA_QUERY = "gemo.uri.efz.data.query";

	static final String SERVICE_URI_GENERAL_EFZ_QUERY_PARAMETER = "gemo.uri.efz.data.query.parameter";

	/** The Constant SERVICE_URI_POSITION_EFZ. */
	static final String SERVICE_URI_POSITION_EFZ = "gemo.uri.efz.position";

	/** The Constant SERVICE_URI_RESERVATION_EFZ. */
	static final String SERVICE_URI_RESERVATION_EFZ = "gemo.uri.efz.reservation";

	/** The Constant SERVICE_RESERVATION_TABLE. */
	static final String SERVICE_RESERVATION_TABLE = "gemo.reservation.table";

	/** The Constant RESERVATION_TYPE_ENUM. */
	static final String RESERVATION_TYPE_ENUM_NAME = "gemo.reservation.type";

	/** The Constant SERVICE_URI_AVAILABILITY_EFZ. */
	static final String SERVICE_URI_AVAILABILITY_EFZ = "gemo.uri.efz.availability";

	static final String SERVICE_URI_AVAILABILITY_EFZ_QUERY = "gemo.uri.efz.availability.query";

	static final String SERVICE_URI_AVAILABILITY_EFZ_METHOD_VEHICLE = "gemo.uri.efz.availability.query.method.vehicle";
	static final String SERVICE_URI_AVAILABILITY_EFZ_METHOD_VEHICLES = "gemo.uri.efz.availability.query.method.vehicles";

	static final String SERVICE_URI_AVAILABILITY_EFZ_QUERY_PARAMETER = "gemo.uri.efz.availability.query.parameter";

	static final String SERVICE_URI_AVAILABILITY_EFZ_UPDATE = "gemo.uri.efz.availability.update";

	/** The Constant SERVICE_URI_COMMON_CHARGING_POINT_DATA. */
	static final String SERVICE_URI_COMMON_CHARGING_POINT_DATA = "gemo.uri.charge.data";

	/** The Constant SERVICE_URI_POSITION_CHARGING_POINT. */
	static final String SERVICE_URI_POSITION_CHARGING_POINT = "gemo.uri.charge.position";

	/** The Constant SERVICE_URI_AVAILABILITY_CHARGING_POINT. */
	static final String SERVICE_URI_AVAILABILITY_CHARGING_POINT = "gemo.uri.charge.availability";

	static final String SERVICE_URI_AVAILABILITY_CHARGING_POINT_QUERY = "gemo.uri.charge.availability.query";

	static final String SERVICE_URI_AVAILABILITY_CHARGING_POINT_UPDATE = "gemo.uri.charge.availability.update";

	static final String SERVICE_URI_AVAILABILITY_CHARGING_POINT_QUERY_PARAMETER = "gemo.uri.charge.availability.query.parameter";

	static final String SERVICE_URI_PARTICIPATION = "gemo.uri.participation";
	static final String SERVICE_URI_PARTICIPATION_COMPLAINT = "gemo.uri.participation.complaint";
	static final String SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_EFZID = "gemo.uri.participation.complaint.query.efzid";
	static final String SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_OFFSET = "gemo.uri.participation.complaint.query.offset";
	static final String SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LIMIT = "gemo.uri.participation.complaint.query.limit";
	static final String SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LAT = "gemo.uri.participation.complaint.query.lat";
	static final String SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LON = "gemo.uri.participation.complaint.query.lon";
	static final String SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_RADIUS = "gemo.uri.participation.complaint.query.radius";

	static final String SERVICE_URI_FIXMYCITY = "gemo.uri.fixmycity";
	static final String SERVICE_URI_FIXMYCITY_COMPLAINT = "gemo.uri.fixmycity.complaint";
	static final String SERVICE_URI_FIXMYCITY_CATEGORY = "gemo.uri.fixmycity.category";
	static final String SERVICE_ACCESS_TO_FIXMYCITY_USERNAME = "gemo.credentials.fixmycity.username";
	static final String SERVICE_ACCESS_TO_FIXMYCITY_PASSWORD = "gemo.credentials.fixmycity.password";

	static final String SERVICE_URI_OPNV = "gemo.uri.opnv";
	static final String SERVICE_URI_OPNV_PARAM_ACCESS = "gemo.uri.opnv.param.access";

	/** The name of the table for the users. */
	static final String USERS_TABLE = "gemo.users.table";

	static final String SERVICES_SCOPES_TABLE = "gemo.servicesScopes.table";
	static final String SCOPES_TABLE = "gemo.scopes.table";

	/** The name of the table for the billing information. */
	static final String PAYMENT_TABLE = "gemo.payment.table";

	/** The name of the table for charging stations. */
	static final String CHARGING_STATION_RESERVATION_TABLE = "gemo.charging.station.resevation.table";

	/** The properties for the user profile service. */
	static final String SERVICE_URI_USERPROFILE = "gemo.uri.userprofile";
	static final String SERVICE_URI_USERPROFILE_ADD_USER = "gemo.uri.userprofile.add_user";

	static final String SERVICE_URL = "gemo.uri";
	static final String SERVICE_APPLICATION_REGISTRATION_DEPLOYMENT_PATH = "gemo.application.registration.deployment.path";
	static final String SERVICE_APPLICATION_REGISTRATION_BINARY_PATH = "gemo.application.registration.binary.path";
	static final String SERVICE_APPLICATION_REGISTRATION_HOTDEPLOY_PATH = "gemo.application.registration.hotdeploy.path";
	static final String CARACCESS_PRIVATE_PATH = "gemo.caraccess.private.path";
	static final String CARACCESS_PUBLIC_PATH = "gemo.caraccess.public.path";
	static final String CARACCESS_SHAREDSECRET_PATH = "gemo.caraccess.sharedsecret.path";

}
