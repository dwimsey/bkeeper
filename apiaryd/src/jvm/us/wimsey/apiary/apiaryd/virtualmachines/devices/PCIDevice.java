package us.wimsey.apiary.apiaryd.virtualmachines.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;

/**
 * Created by dwimsey on 7/10/16.
 */
public abstract class PCIDevice implements IVMDevice {
	private static final Logger logger = LogManager.getLogger(PCIDevice.class);

	public PCIDevice(int bus, int slot, int function)
	{
		_bus = bus;
		_slot = slot;
		_function = function;
	}
	protected boolean _enabled = true;

	protected int _bus = -1;
	protected int _slot = -1;
	protected int _function = -1;

	public String getDeviceAddress() {
		return _bus + ":" + _slot + ":" + _function;
	}
}
