package us.wimsey.apiary.apiaryd.restlets;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * Created by dwimsey on 3/30/16.
 */
public class PingHandler extends RouterNanoHTTPD.DefaultHandler {

	@Override
	public String getText() {
		return "{\n \"pong\": \"" + System.currentTimeMillis() + "\"\n} ";
	}

	@Override
	public String getMimeType() {
		return "text/html";
	}

	@Override
	public NanoHTTPD.Response.IStatus getStatus() {
		return NanoHTTPD.Response.Status.OK;
	}

	public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
		// urlParams, session
		String text;
		if (urlParams.isEmpty()) {
				text = getText();
		} else {
			text = "{\n \"ping\": \"" + urlParams.values().toArray()[0] + "\",\n" +
					" \"pong\": \"" + System.currentTimeMillis() + "\"\n} ";
		}
		ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
		int size = text.getBytes().length;
		return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), inp, size);
	}

}
