package us.wimsey.apiary.apiaryd.virtualmachines.devices.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.LpcIsaPciBridge;

import javax.xml.xpath.*;

/**
 * Created by dwimsey on 7/11/16.
 */
public class LPCBridgeFactory implements VMDeviceFactory {
	private static final Logger logger = LogManager.getLogger(LPCBridgeFactory.class);

	public LPCBridgeFactory(IHypervisor bhyveHypervisorDriver) {
	}

	@Override
	public IVMDevice parseDeviceNode(Node deviceNode) {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();

		String bootRomStr = null;
		boolean bootRomEnabled = false;
		String com1Str = null;
		boolean com1Enabled = false;
		String com2Str = null;
		boolean com2Enabled = false;

		try {
			String bootRomEnabledStr = (String) xpath.evaluate("bootrom/@enabled", deviceNode, XPathConstants.STRING);
			bootRomStr = (String) xpath.evaluate("bootrom/@path", deviceNode, XPathConstants.STRING);
			if((bootRomEnabledStr == null || bootRomEnabledStr.isEmpty() == true) && (bootRomStr != null && bootRomStr.isEmpty() == false)) {
				// We don't have an enabled attribute, but we do have a path, assume enabled
				bootRomEnabled = true;
			} else if(bootRomEnabledStr != null && bootRomEnabledStr.isEmpty() == false){
				// We do have a bootrom enabled attribute, test it
				if(Boolean.valueOf(bootRomEnabledStr) == true) {
					bootRomEnabled = true;
				}
			}

			if(bootRomStr != null) {
				if(bootRomStr.isEmpty() == false) {
					// validate bootRomStr
				} else {
					bootRomStr = null;
				}
			}
			logger.debug("lpc: bootrom: " + (bootRomEnabled ? "enabled" : "disabled") + ": " + bootRomStr);
		} catch (XPathExpressionException e) {
			logger.error("lpc: bootrom: configuration parsing error: " + e.toString() + " Node: " + deviceNode.toString());
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		try {
			String com1EnabledStr = (String) xpath.evaluate("com1/@enabled", deviceNode, XPathConstants.STRING);
			com1Str = (String) xpath.evaluate("com1/@path", deviceNode, XPathConstants.STRING);
			if((com1EnabledStr == null || com1EnabledStr.isEmpty() == true) && (com1Str != null && com1Str.isEmpty() == false)) {
				// We don't have an enabled attribute, but we do have a path, assume enabled
				com1Enabled = true;
			} else if(com1EnabledStr != null && com1EnabledStr.isEmpty() == false){
				// We do have a bootrom enabled attribute, test it
				if(Boolean.valueOf(com1EnabledStr) == true) {
					com1Enabled = true;
				}
			}

			if(com1Str != null) {
				if(com1Str.isEmpty() == false) {
					// validate com1Str
				} else {
					com1Str = null;
				}
			}
			logger.debug("lpc: com1: " + (com1Enabled ? "enabled" : "disabled") + ": " + com1Str);
		} catch (XPathExpressionException e) {
			logger.error("lpc: com1: configuration parsing error: " + e.toString() + " Node: " + deviceNode.toString());
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		try {
			String com2EnabledStr = (String) xpath.evaluate("com2/@enabled", deviceNode, XPathConstants.STRING);
			com2Str = (String) xpath.evaluate("com2/@path", deviceNode, XPathConstants.STRING);
			if((com2EnabledStr == null || com2EnabledStr.isEmpty() == true) && (com2Str != null && com2Str.isEmpty() == false)) {
				// We don't have an enabled attribute, but we do have a path, assume enabled
				com2Enabled = true;
			} else if(com2EnabledStr != null && com2EnabledStr.isEmpty() == false){
				// We do have a bootrom enabled attribute, test it
				if(Boolean.valueOf(com2EnabledStr) == true) {
					com2Enabled = true;
				}
			}

			if(com2Str != null) {
				if(com2Str.isEmpty() == false) {
					// validate com2Str
				} else {
					com2Str = null;
				}
			}
			logger.debug("lpc: com2: " + (com2Enabled ? "enabled" : "disabled") + ": " + com2Str);
		} catch (XPathExpressionException e) {
			logger.error("lpc: com2: configuration parsing error: " + e.toString() + " Node: " + deviceNode.toString());
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}


		return new LpcIsaPciBridge(bootRomStr, com1Str, com2Str);
	}}
