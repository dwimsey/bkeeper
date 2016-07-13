package us.wimsey.apiary.apiaryd.virtualmachines.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static us.wimsey.apiary.apiaryd.virtualmachines.devices.BlockDevice.DeviceType.*;

/**
 * Created by dwimsey on 7/10/16.
 */
public class BlockDevice extends PCIDevice {
	private static final Logger logger = LogManager.getLogger(BlockDevice.class);

	public BlockDevice(String deviceClass, int bus, int slot, int function, String path, boolean sync, boolean noCache, boolean readOnly) {
		super(bus, slot, function);
		_deviceType = parseValue(deviceClass);
		_path = path;
		_sync = sync;
		_nocache = noCache;
		_readonly = readOnly;
	}

	public enum DeviceType {
		virtioblk (1),
		ahcicd (2),
		ahcihd (3);

		final private int _deviceType;
		DeviceType(int dType)
		{
			_deviceType = dType;
		}

		static public DeviceType parseValue(String deviceName)
		{
			switch(deviceName.toLowerCase()) {
				case "ahci-cd":
					return ahcicd;
				case "ahci-hd":
					return ahcihd;
				case "virtio-blk":
					return virtioblk;
				default:
					return valueOf(deviceName);
			}
		}

		public String toString()
		{
			switch(_deviceType) {
				case 1:
					return "virtio-blk";
				case 2:
					return "ahci-cd";
				case 3:
					return "ahci-hd";
			}
			return "unknown";
		}
	}

	private DeviceType _deviceType = virtioblk;
	@Override
	public String getDeviceName() {
		return _deviceType.toString();
	}

	public String toString()
	{
		String respStr = "Unknown Block Device: ";
		switch(_deviceType) {
			case virtioblk:
				respStr = "VirtIO Block Device: ";
				break;
			case ahcicd:
				respStr = "AHCI  CDROM  Device: ";
				break;
			case ahcihd:
				respStr = "AHCI HDD/SSD Device: ";
				break;
		}
		return (respStr + (_enabled ? "enabled " : "disabled") + " path: " + _path + " flags: " +
				(_sync ? "sync ": "") + (_nocache ? "nocache ": "") + (_readonly ? "readonly": ""));
	}

	private int _bus;
	private int _slot;
	private int _function;

	public String getCmdline()
	{
		String configString = " -s " + this.getDeviceAddress() + "," + _deviceType.toString() + "," + _path;
		if(_sync == true) {
			configString += ",direct";
		}
		if(_nocache == true) {
			configString += ",nocache";
		}
		if(_readonly == true) {
			configString += ",ro";
		}
		return configString;
	}

	String _path = null;
	public void setPath(String URL)
	{
		_path = URL;
	}
	public String getPath()
	{
		return _path;
	}

	boolean _nocache = false;
	boolean _sync = false;
	boolean _readonly = false;
}
