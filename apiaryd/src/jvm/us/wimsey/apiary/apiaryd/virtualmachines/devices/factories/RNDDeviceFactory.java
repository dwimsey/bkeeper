package us.wimsey.apiary.apiaryd.virtualmachines.devices.factories;

import org.w3c.dom.Node;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;
import us.wimsey.apiary.apiaryd.hypervisors.xhyve.XhyveHypervisorDriver;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.RandomNumberGeneratorDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.factories.VMDeviceFactory;

/**
 * Created by dwimsey on 7/12/16.
 */
public class RNDDeviceFactory implements VMDeviceFactory {
	public RNDDeviceFactory(IHypervisor hostHypervisor) {
	}

	@Override
	public IVMDevice parseDeviceNode(Node deviceNode, String deviceClass, int bus, int slot, int function) {
		return new RandomNumberGeneratorDevice(bus, slot, function);
	}
}
