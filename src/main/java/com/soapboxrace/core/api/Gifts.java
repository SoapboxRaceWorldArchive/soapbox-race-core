package com.soapboxrace.core.api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.soapboxrace.jaxb.http.ArrayOfLevelGiftDefinition;

@Path("/Gifts")
public class Gifts {

	@POST
	@Path("/GetAndTriggerAvailableLevelGifts")
	@Produces(MediaType.APPLICATION_XML)
	public ArrayOfLevelGiftDefinition getAndTriggerAvailableLevelGifts() {
		return new ArrayOfLevelGiftDefinition();
	}

}
