package us.wimsey.apiary.apiaryd.virtualmachines.devices.factories;

import org.w3c.dom.Node;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;

/**
 * Created by dwimsey on 7/11/16.
 */
public interface VMDeviceFactory {
	IVMDevice parseDeviceNode(Node deviceNode);
}
