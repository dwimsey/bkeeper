package us.wimsey.apiary.apiaryd.virtualmachines.devices;

import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;

/**
 * Created by dwimsey on 7/10/16.
 */
public interface IVMDevice {
	public String getDeviceName();
	public String getDeviceAddress();
	public String getCmdline();
}
