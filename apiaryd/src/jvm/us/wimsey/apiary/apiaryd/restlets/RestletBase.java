package us.wimsey.apiary.apiaryd.restlets;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * Created by dwimsey on 3/30/16.
 */
public class RestletBase implements RouterNanoHTTPD.UriResponder {

	public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
		String text = "GET is not implemented for this request.";
		ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
		int size = text.getBytes().length;
		return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_IMPLEMENTED, "text/html", inp, size);
	}

	public NanoHTTPD.Response put(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
		String text = "PUT is not implemented for this request.";
		ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
		int size = text.getBytes().length;
		return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_IMPLEMENTED, "text/html", inp, size);
	}

	public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
		String text = "POST is not implemented for this request.";
		ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
		int size = text.getBytes().length;
		return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_IMPLEMENTED, "text/html", inp, size);
	}

	public NanoHTTPD.Response delete(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
		String text = "DELETE is not implemented for this request.";
		ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
		int size = text.getBytes().length;
		return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_IMPLEMENTED, "text/html", inp, size);
	}

	public NanoHTTPD.Response other(String method, RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
		String text = method.toUpperCase() + " is not implemented for this request.";
		ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
		int size = text.getBytes().length;
		return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_IMPLEMENTED, "text/html", inp, size);
	}

}
