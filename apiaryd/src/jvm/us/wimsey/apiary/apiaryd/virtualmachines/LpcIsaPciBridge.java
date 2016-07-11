package us.wimsey.apiary.apiaryd.virtualmachines;

/**
 * Created by dwimsey on 7/10/16.
 */
public class LpcIsaPciBridge extends PCIDevice {
	@Override
	public String getDeviceName() {
		return "lpc";
	}

	@Override
	public String getDeviceAddress() {
		return "31";
	}

	private int _bus;
	private int _slot;
	private int _function;
	public String configureDevice(int bus, int slot)
	{
		if(bus != 0) {
			throw new IllegalArgumentException("LPC ISA-PCI Bridge for legacy devices (lpc) must be on bus 0.  Configured bus: " + bus);
		}

		// -s 31,lpc is required for base connection
		String configString = "lpc";
		if(_bootrom != null) {
			// -l bootroom,<PATH>
			configString += " -l bootrom," + _bootrom;
		}
		if(_com1 != null) {
			configString += " -l com1," + _com1;
		}
		if(_com2 != null) {
			configString += " -l com2," + _com2;
		}
		return configString;
	}

	String _bootrom = null;
	public void setBootromFile(String URL)
	{
		_bootrom = URL;
	}
	public String getBootrom()
	{
		return _bootrom;
	}

	String _com1 = null;
	public void setCom1DeviceFile(String URL)
	{
		_com1 = URL;
	}
	public String getCom1DeviceFile()
	{
		return _com1;
	}

	String _com2 = null;
	public void setCom2DeviceFile(String URL)
	{
		_com2 = URL;
	}
	public String getCom2DeviceFile()
	{
		return _com2;
	}
}
