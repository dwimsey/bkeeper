package us.wimsey.apiary.apiaryd.virtualmachines.devices.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.PCIHostBridge;

import javax.xml.xpath.*;

/**
 * Created by dwimsey on 7/12/16.
 */
public class PCIHostbridgeFactory implements VMDeviceFactory {
	private static final Logger logger = LogManager.getLogger(PCIHostbridgeFactory.class);

	public PCIHostbridgeFactory(IHypervisor bhyveHypervisorDriver) {
	}

	@Override
	public IVMDevice parseDeviceNode(Node deviceNode) {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = null;
		try {
			expr = xpath.compile("@type");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		PCIHostBridge.HostbridgeOperatingMode bridgeType = PCIHostBridge.HostbridgeOperatingMode.Intel;
		try {
			String bridgeTypeString = (String) expr.evaluate(deviceNode, XPathConstants.STRING);
			if("intel".compareToIgnoreCase(bridgeTypeString) == 0) {
				bridgeType = PCIHostBridge.HostbridgeOperatingMode.Intel;
			} else if("amd".compareToIgnoreCase(bridgeTypeString) == 0) {
				bridgeType = PCIHostBridge.HostbridgeOperatingMode.Amd;
			} else {
				throw new IllegalArgumentException("Invalid operating mode specified for hostbridge, expecting 'intel' or 'amd', got: " + bridgeTypeString);
			}
			logger.debug("bridgeType: " + bridgeType);

		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		return new PCIHostBridge(bridgeType);
	}
}
