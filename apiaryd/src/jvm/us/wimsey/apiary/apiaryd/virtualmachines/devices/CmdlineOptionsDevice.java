package us.wimsey.apiary.apiaryd.virtualmachines.devices;


/**
 * Created by dwimsey on 7/12/16.
 */
public class CmdlineOptionsDevice extends PCIDevice {
	String _options = "";
	public CmdlineOptionsDevice(int bus, int slot, int function, String options) {
		super(bus, slot, function);
		_options = options;
	}

	@Override
	public String getDeviceName() {
		return "hvmoptions";
	}

	public String toString()
	{
		return ("HVM Options: " + _options);
	}
	public String getCmdline()
	{
		return " " + _options;
	}
}
