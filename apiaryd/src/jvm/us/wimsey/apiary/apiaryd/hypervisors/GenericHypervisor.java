package us.wimsey.apiary.apiaryd.hypervisors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import us.wimsey.apiary.apiaryd.hypervisors.xhyve.XhyveVMState;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;
import us.wimsey.apiary.apiaryd.virtualmachines.VMStateBase;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.factories.VMDeviceFactory;

import javax.xml.xpath.XPathConstants;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dwimsey on 7/10/16.
 */
public abstract class GenericHypervisor implements IHypervisor {
	private static final Logger logger = LogManager.getLogger(GenericHypervisor.class);

	protected Map<String, VMDeviceFactory> deviceFactories = new HashMap<String, VMDeviceFactory>();

	final protected Properties _props;
	public GenericHypervisor(Properties hypervisorInitializationProperties)
	{
		_props = hypervisorInitializationProperties;
	}

	protected String shellCmd = "/bin/sh ${SHELLCMD}";

	public String getHypervisorName()
	{
		String hostname = "*Unknown*";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostname;
	}

	public List<String> getVMList()
	{
		throw new NotImplementedException();
	}

	public IVMState registerVm(String URL) {
		return registerVm(new File(URL));
	}

	public void shutdown(float gracePeriod)
	{
	}

	public IVMDevice parseDevice(Node devNode) {
		IVMDevice newDevice = null;
		Node busTypeNode = devNode.getAttributes().getNamedItem("bustype");
		String busType = "pci";
		if(busTypeNode != null) {
			if(busType.equals(busTypeNode.getNodeValue()) == false) {
				throw new IllegalArgumentException("Only 'pci' is currently supported for bustype.");
			}
		}

		String deviceClass = devNode.getNodeName();

		switch(busType) {
			default:
			case "pci":
				int bus = Integer.parseInt(devNode.getAttributes().getNamedItem("bus").getNodeValue());
				int slot = Integer.parseInt(devNode.getAttributes().getNamedItem("slot").getNodeValue());
				int function = Integer.parseInt(devNode.getAttributes().getNamedItem("function").getNodeValue());
				if(deviceFactories.containsKey(deviceClass) == true) {
					// We have a factory for this class, lets create it
					VMDeviceFactory df = deviceFactories.get(deviceClass);
					newDevice = df.parseDeviceNode(devNode, deviceClass, bus, slot, function);
				} else {
					throw new IllegalArgumentException("Unknown device class specified: " + deviceClass);
				}
				break;
		}
		return newDevice;
	}
}
