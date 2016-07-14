package us.wimsey.apiary.apiaryd.virtualmachines.devices.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.*;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by dwimsey on 7/12/16.
 */
public class CmdLineOptionsDeviceFactory implements VMDeviceFactory {
	private static final Logger logger = LogManager.getLogger(CmdLineOptionsDeviceFactory.class);

	public CmdLineOptionsDeviceFactory(IHypervisor hostHypervisor) {
	}

	@Override
	public IVMDevice parseDeviceNode(Node deviceNode, String deviceClass, int bus, int slot, int function) {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();

		String optionsString = "";
		try {
			optionsString = (String) xpath.evaluate("@options", deviceNode, XPathConstants.STRING);
		} catch (XPathExpressionException ex)  {
			logger.error("Could not extract options: " + deviceClass + ": " + ex);
		}

		return new CmdlineOptionsDevice(bus, slot, function, optionsString);
	}
}
