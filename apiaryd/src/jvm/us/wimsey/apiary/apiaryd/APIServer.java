package us.wimsey.apiary.apiaryd;

import java.util.Properties;


import fi.iki.elonen.router.RouterNanoHTTPD;
import us.wimsey.apiary.apiaryd.restlets.PingHandler;

/**
 * Created by dwimsey on 1/25/16.
 */
public class APIServer extends RouterNanoHTTPD {
	public static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(APIServer.class);

	Properties _properties;
	public APIServer(int port, Properties newProperties) {
		super(port);
		_properties = newProperties;

		//router.setNotImplemented(NotImplementedHandler.class);
		//router.setNotFoundHandler(Error404UriHandler.class);

		super.addMappings();
		// The following for lines replace the above line
		removeRoute("/");
		removeRoute("/index.");
		addRoute("/debug", GeneralHandler.class);
		addRoute("/debug/:param1", GeneralHandler.class);
		addRoute("/ping", PingHandler.class);
		addRoute("/ping/:timestamp", PingHandler.class);
	}

	public APIServer(int port) {
		super(port);
		_properties = new Properties();
	}
}
