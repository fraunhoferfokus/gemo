package de.fhg.fokus.mdc.utils.auth;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Jersey HTTP Basic Auth filter
 * 
 * @auhor dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 */
public class GlobalAuthenticationResponseFilter implements
		ContainerResponseFilter {

	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {

		// not used at the moment

		return response;
	}

}