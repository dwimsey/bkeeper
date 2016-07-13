package us.wimsey.apiary.apiaryd.virtualmachines.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;

/**
 * Created by dwimsey on 7/12/16.
 */
public class PCIHostBridge extends PCIDevice {
	private static final Logger logger = LogManager.getLogger(PCIHostBridge.class);

	public PCIHostBridge(String deviceClass, int bus, int slot, int function, HostbridgeOperatingMode bridgeType) {
		super(bus, slot, function);
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

	public String getCmdline()
	{
		if(_bus != 0) {
			throw new IllegalArgumentException("PCI host bridge must be on bus 0.  Configured bus: " + _bus);
		}
		if(_slot != 0) {
			throw new IllegalArgumentException("PCI host bridge must be on bus 0, slot 0.  Configured slot: " + _slot);
		}
		if(_function != 0) {
			throw new IllegalArgumentException("PCI host bridge must be on bus 0, slot 0, function 0.  Configured function: " + _function);
		}
		switch(_operatingMode) {
			case Intel:
				return " -s 0:0:0,hostbridge";
			case Amd:
				return " -s 0:0:0,amd_hostbridge";
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
