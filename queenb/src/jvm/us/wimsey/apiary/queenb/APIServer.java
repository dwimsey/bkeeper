package us.wimsey.apiary.queenb;

import java.io.IOException;
import java.util.Properties;


import java.util.Map;
import java.util.logging.Logger;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;

/**
 * Created by dwimsey on 1/25/16.
 */
public class APIServer extends NanoHTTPD {
	public static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(APIServer.class);

	Properties _properties;
	public APIServer(int port, Properties newProperties) {
		super(port);
		_properties = newProperties;
	}

	public APIServer(int port) {
		super(port);
	}

	@Override
	public Response serve(IHTTPSession session) {
		Method method = session.getMethod();
		String uri = session.getUri();
		LOG.info(method + " '" + uri + "' ");

		String msg;
		String parts[] = uri.split("/", 4);
		if("api".equals(parts[1]) == false) {
			msg = "<html><body><h1>Hello server</h1>\n";
			Map<String, String> parms = session.getParms();
			if (parms.get("username") == null) {
				msg += "<form action='?' method='get'>\n" + "  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
			} else {
				msg += "<p>Hello, " + parms.get("username") + "!</p>";
			}

			msg += "</body></html>\n";

			return newFixedLengthResponse(msg);
		}

		String container = parts[2];
		msg = "<html><body><h1>Listing: " + container + "</h1>\n";
		if("vms".equals(container) == true) {
			msg += "Listing virtual machines:\n";
		} else {
			msg += "Unknown object container: " + container + "\n";
		}
		msg += "</body></html>\n";

		return newFixedLengthResponse(msg);

	}

}
