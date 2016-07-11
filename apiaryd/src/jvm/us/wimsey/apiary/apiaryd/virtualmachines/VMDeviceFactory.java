package us.wimsey.apiary.apiaryd.virtualmachines;

import org.w3c.dom.Node;

/**
 * Created by dwimsey on 7/11/16.
 */
public interface VMDeviceFactory {
	IVMDevice parseDeviceNode(Node deviceNode);
}
