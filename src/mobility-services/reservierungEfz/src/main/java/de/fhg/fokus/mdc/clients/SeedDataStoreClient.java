package de.fhg.fokus.mdc.clients;

public class SeedDataStoreClient extends DataStoreClient {
	/** The singleton instances of the class. */
	private static SeedDataStoreClient instance = null;

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static SeedDataStoreClient getInstance() {
		if (instance == null) {
			instance = new SeedDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	private SeedDataStoreClient() {
		super();

	}

	public String getByQuery(String seedQuery) {

		return select(seedQuery);
	}

	@Override
	public void defineTableName() {
		// TODO Auto-generated method stub

	}
}
