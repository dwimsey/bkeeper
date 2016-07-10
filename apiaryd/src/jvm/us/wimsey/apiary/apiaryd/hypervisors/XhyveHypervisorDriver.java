package us.wimsey.apiary.apiaryd.hypervisors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class XhyveHypervisorDriver extends GenericHypervisor {
	private static final Logger logger = LogManager.getLogger(XhyveHypervisorDriver.class);

	public XhyveHypervisorDriver(Properties hypervisorInitializationProperties)
	{
		super(hypervisorInitializationProperties);
	}
}
