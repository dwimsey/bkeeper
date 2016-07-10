package us.wimsey.apiary.apiaryd.hypervisors;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class BhyveHypervisorDriver implements IHypervisor {
	final Properties _props;
	public BhyveHypervisorDriver(Properties hypervisorInitializationProperties)
	{
		_props = hypervisorInitializationProperties;
	}

	public String getHypervisorName()
	{
		String hostname = "*Unknown*";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostname;
	}
}
