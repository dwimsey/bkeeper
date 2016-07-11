package us.wimsey.apiary.apiaryd.virtualmachines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by dwimsey on 7/10/16.
 */
public abstract class PCIDevice implements IVMDevice {
	private static final Logger logger = LogManager.getLogger(PCIDevice.class);

	@Override
	public String getDeviceAddress() {
		return null;
	}
}
