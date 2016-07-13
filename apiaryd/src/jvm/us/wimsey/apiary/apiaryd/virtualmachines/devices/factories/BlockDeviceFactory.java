package us.wimsey.apiary.apiaryd.virtualmachines.devices.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.BlockDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.InvalidPCIDevice;

import javax.xml.xpath.*;

/**
 * Created by dwimsey on 7/12/16.
 */
public class BlockDeviceFactory implements VMDeviceFactory {
	private static final Logger logger = LogManager.getLogger(BlockDeviceFactory.class);

	public BlockDeviceFactory(IHypervisor bhyveHypervisorDriver) {
	}

	@Override
	public IVMDevice parseDeviceNode(Node deviceNode, String deviceClass, int bus, int slot, int function) {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();

		boolean noCache = false;
		boolean sync = false;
		boolean readOnly = false;

		try {
			BlockDevice.DeviceType ndType = BlockDevice.DeviceType.parseValue(deviceClass);
		} catch(Exception ex) {
			logger.error("Could not parse node name for device type: " + deviceClass + ": " + ex);
			return new InvalidPCIDevice(deviceNode.getTextContent());
		}

		String path = null;
		try {
			path = (String) xpath.evaluate("@path", deviceNode, XPathConstants.STRING);
			logger.debug(deviceClass + ": path: " + path);
		} catch (XPathExpressionException ex) {
			logger.error("Could not extract path for device: " + deviceClass + ": " + ex);
			return new InvalidPCIDevice(deviceNode.getTextContent());
		}

		String av;
		try {
			av = (String) xpath.evaluate("@nocache", deviceNode, XPathConstants.STRING);
			logger.debug(deviceClass + ": nocache: " + av);
			noCache = Boolean.valueOf(av);
		} catch (XPathExpressionException ex) {
			logger.error("Could not extract nocache value for device: " + deviceClass + ": " + ex);
			return new InvalidPCIDevice(deviceNode.getTextContent());
		}

		try {
			av = (String) xpath.evaluate("@sync", deviceNode, XPathConstants.STRING);
			logger.debug(deviceClass + ": sync: " + av);
			sync = Boolean.valueOf(av);
		} catch (XPathExpressionException ex) {
			logger.error("Could not extract sync value for device: " + deviceClass + ": " + ex);
			return new InvalidPCIDevice(deviceNode.getTextContent());
		}

		try {
			av = (String) xpath.evaluate("@readonly", deviceNode, XPathConstants.STRING);
			logger.debug(deviceClass + ": readonly: " + av);
			readOnly = Boolean.valueOf(av);
		} catch (XPathExpressionException ex) {
			logger.error("Could not extract readonly value for device: " + deviceClass + ": " + ex);
			return new InvalidPCIDevice(deviceNode.getTextContent());
		}

		return new BlockDevice(deviceClass, bus, slot, function, path, sync, noCache, readOnly);
	}
}
