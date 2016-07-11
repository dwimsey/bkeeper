package us.wimsey.apiary.apiaryd;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.restlets.PingHandler;
import us.wimsey.apiary.apiaryd.restlets.RestletBase;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dwimsey on 1/25/16.
 */
public class APIServer extends RouterNanoHTTPD {
	private static final Logger logger = LogManager.getLogger(APIServer.class);

	Properties _properties = null;
	VMMonitor _vmMonitor = null;

	public APIServer(int port, Properties newProperties, VMMonitor vmMonitor) {
		super(port);
		_properties = newProperties;
		_vmMonitor = vmMonitor;

		//router.setNotImplemented(NotImplementedHandler.class);
		//router.setNotFoundHandler(Error404UriHandler.class);

		super.addMappings();
		// The following for lines replace the above line
		removeRoute("/");
		removeRoute("/index.html");


		addRoute("/debug", GeneralHandler.class);
		addRoute("/debug/:param1", GeneralHandler.class);
		addRoute("/ping", PingHandler.class);
		addRoute("/ping/:timestamp", PingHandler.class);
		addRoute("/vms", vms.class);
	}

	public APIServer(int port) {
		super(port);
		_properties = new Properties();
	}


	public class vms extends RestletBase {

		@Override
		public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
			// urlParams, session
			String text;
			if (urlParams.isEmpty()) {
				text = "{\n \"pong\": \"" + System.currentTimeMillis() + "\"\n} ";
			} else {
				text = "{\n \"ping\": \"" + urlParams.values().toArray()[0] + "\",\n" +
						" \"pong\": \"" + System.currentTimeMillis() + "\"\n} ";
			}
			ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
			int size = text.getBytes().length;
			return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", inp, size);
		}
	}
}
