package us.wimsey.apiary.apiaryd.virtualmachines.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by dwimsey on 7/10/16.
 */
public abstract class PCIDevice implements IVMDevice {
	private static final Logger logger = LogManager.getLogger(PCIDevice.class);

	protected boolean _enabled = true;

	@Override
	public String getDeviceAddress() {
		return null;
	}
}
