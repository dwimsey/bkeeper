package us.wimsey.apiary.apiaryd.virtualmachines.devices.factories;

import org.w3c.dom.Node;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;
import us.wimsey.apiary.apiaryd.hypervisors.xhyve.XhyveHypervisorDriver;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.NICDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.factories.VMDeviceFactory;

/**
 * Created by dwimsey on 7/12/16.
 */
public class NICDeviceFactory implements VMDeviceFactory {
	public NICDeviceFactory(IHypervisor hostHypervisor) {
	}

	@Override
	public IVMDevice parseDeviceNode(Node deviceNode, String deviceClass, int bus, int slot, int function) {
		String driverName = "virtio-net";
		String networkName = "tap0";
		String hwaddr = null;
		return new NICDevice(deviceClass, bus, slot, function, networkName, hwaddr);
	}
}
