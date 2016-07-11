package us.wimsey.apiary.apiaryd.virtualmachines;

/**
 * Created by dwimsey on 7/10/16.
 */
public interface IVMDevice {
	public String getDeviceName();
	public String getDeviceAddress();
	public String configureDevice(int bus, int slot);
}
