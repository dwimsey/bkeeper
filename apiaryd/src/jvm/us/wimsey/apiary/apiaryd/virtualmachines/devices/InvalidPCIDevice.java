package us.wimsey.apiary.apiaryd.virtualmachines.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by dwimsey on 7/10/16.
 */
public class InvalidPCIDevice extends PCIDevice {
	private static final Logger logger = LogManager.getLogger(InvalidPCIDevice.class);

	private String _failedConfigurationString = null;
	public InvalidPCIDevice(String failedConfigurationString) {
		super(-2, -2, -2);
		_failedConfigurationString = failedConfigurationString;
	}

	@Override
	public String getDeviceName() {
		return "invalid";
	}

	@Override
	public String getDeviceAddress() {
		return "-1:-1:-1";
	}

	public String getCmdline()
	{
		return "";
	}
}
