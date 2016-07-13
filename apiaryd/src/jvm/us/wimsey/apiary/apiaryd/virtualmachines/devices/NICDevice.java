package us.wimsey.apiary.apiaryd.virtualmachines.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static us.wimsey.apiary.apiaryd.virtualmachines.devices.NICDevice.NICType.parseValue;

/**
 * Created by dwimsey on 7/10/16.
 */
public class NICDevice extends PCIDevice {
	private static final Logger logger = LogManager.getLogger(NICDevice.class);

	public NICDevice(String deviceClass, int bus, int slot, int function, String device, String hwaddr) {
		super(bus, slot, function);
		_nicType = parseValue(deviceClass);
		_device = device;
		_hwaddr = hwaddr;
	}

	public enum NICType {
		virtionet(1),
		tap(2),
		vmnet(3);


		final private int _nicType;
		NICType(int dType)
		{
			_nicType = dType;
		}

		static public NICType parseValue(String deviceName)
		{
			switch(deviceName.toLowerCase()) {
				case "virtio-net":
					return virtionet;
				default:
					return valueOf(deviceName);
			}
		}

		public String toString()
		{
			switch(_nicType) {
				case 1:
					return "virtio-net";
			}
			return "unknown";
		}
	}

	private NICType _nicType = NICType.virtionet;
	@Override
	public String getDeviceName() {
		return _nicType.toString();
	}

	String _hwaddr;

	public String toString()
	{
		return  "virtio-net: "+ (_enabled ? "enabled " : "disabled") + " device: " + _device + " hwaddr: " + _hwaddr;
	}

	private int _bus;
	private int _slot;
	private int _function;

	public String getCmdline()
	{
		String configString = " -s " + this.getDeviceAddress() + "," + _nicType.toString() + "," + _device;
		if(_hwaddr != null && _hwaddr.isEmpty() == false) {
			configString = configString + ",mac=" + _hwaddr;
		}
		return configString;
	}

	String _device = null;
	public void setPath(String URL)
	{
		_device = URL;
	}
	public String getPath()
	{
		return _device;
	}

	boolean _nocache = false;
	boolean _sync = false;
	boolean _readonly = false;
}
