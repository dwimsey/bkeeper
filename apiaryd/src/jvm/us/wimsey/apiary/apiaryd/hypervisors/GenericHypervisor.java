package us.wimsey.apiary.apiaryd.hypervisors;

import com.sun.tools.javac.jvm.Gen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 7/10/16.
 */
public class GenericHypervisor implements IHypervisor {
	private static final Logger logger = LogManager.getLogger(GenericHypervisor.class);

	final protected Properties _props;
	public GenericHypervisor(Properties hypervisorInitializationProperties)
	{
		_props = hypervisorInitializationProperties;
	}

	protected String shellCmd = "/bin/sh ${SHELLCMD}";

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

	public List<String> getVMList()
	{
		throw new NotImplementedException();
	}

	public void shutdown(float gracePeriod)
	{
	}


}
