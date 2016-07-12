package us.wimsey.apiary.apiaryd.virtualmachines.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by dwimsey on 7/12/16.
 */
public class PCIHostBridge extends PCIDevice {
	private static final Logger logger = LogManager.getLogger(PCIHostBridge.class);

	public PCIHostBridge(HostbridgeOperatingMode bridgeType) {
		super();
		_operatingMode = bridgeType;
	}

	public String toString()
	{
		return ("PCI Hostbridge: " + _operatingMode.toString() + (_enabled ? ": enabled" : ": disabled"));
	}

	@Override
	public String getDeviceName() {
		return "hostbridge";
	}

	@Override
	public String getDeviceAddress() {
		return _bus + ":" + _slot + ":" + _function;
	}

	private int _bus;
	private int _slot;
	private int _function;
	public String configureDevice(int bus, int slot)
	{
		if(bus != 0) {
			throw new IllegalArgumentException("PCI host bridge must be on bus 0.  Configured bus: " + bus);
		}
		_bus = bus;

		if(slot != 0) {
			throw new IllegalArgumentException("PCI host bridge must be on bus 0, slot 0.  Configured slot: " + slot);
		}
		_slot = slot;

		int function = 0; // place holder to make code consistent 'looking'
		if(function != 0) {
			throw new IllegalArgumentException("PCI host bridge must be on bus 0, slot 0, function 0.  Configured function: " + function);
		}
		_function = 0;

		switch(_operatingMode) {
			case Intel:
				return "hostbridge";
			case Amd:
				return "amd_hostbridge";
			default:
				throw new IllegalArgumentException("Unexpected operating mode: " + _operatingMode.toString());
		}
	}

	public enum HostbridgeOperatingMode {
		Intel (1),
		Amd (2);

		private int _typeId;
		HostbridgeOperatingMode(int typeId)
		{
			this._typeId = typeId;
		}
	}
	HostbridgeOperatingMode _operatingMode = null;
	public void setVendorType(HostbridgeOperatingMode operatingMode)
	{
		_operatingMode = operatingMode;
	}
	public HostbridgeOperatingMode getVendorType()
	{
		return _operatingMode;
	}
}
