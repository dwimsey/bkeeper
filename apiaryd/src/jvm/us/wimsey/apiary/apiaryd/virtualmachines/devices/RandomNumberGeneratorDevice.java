package us.wimsey.apiary.apiaryd.virtualmachines.devices;


import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;

/**
 * Created by dwimsey on 7/12/16.
 */
public class RandomNumberGeneratorDevice extends PCIDevice {
	public RandomNumberGeneratorDevice(int bus, int slot, int function) {
		super(bus, slot, function);
	}

	@Override
	public String getDeviceName() {
		return "virtio-rnd";
	}

	public String toString()
	{
		return ("VirtIO Random Number Generator: " + (_enabled ? "enabled" : "disabled"));
	}
	public String getCmdline()
	{
		return " -s " + getDeviceAddress() +",virtio-rnd";
	}
}
