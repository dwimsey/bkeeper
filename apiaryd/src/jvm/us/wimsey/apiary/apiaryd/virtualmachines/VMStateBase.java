package us.wimsey.apiary.apiaryd.virtualmachines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by dwimsey on 7/10/16.
 */
public abstract class VMStateBase implements IVMState {
	private static final Logger logger = LogManager.getLogger(VMStateBase.class);

	protected UUID _SMBUUID;
	protected VMStateBase()
	{
		_SMBUUID = UUID.randomUUID();
		// Until more is done, the VM configuration is invalid, this is okay for now, not
		// really an error just a statement of fact.
		validateConfig();
	}

	private void validateConfig()
	{

	}

	public UUID getUuid() {
		return _SMBUUID;
	}

	public void setUuid(UUID newUUID) { _SMBUUID = newUUID; }

	public boolean getIgnoreMissingMSRs() {
		return true;
	}

	public boolean getForceVirtIOToUseMSI() {
		return false;
	}

	public boolean getAPICx2Mode() {
		return false;
	}

	public boolean getMPTableDisabled() {
		return false;
	}

	public int[][] getCpuPinMap() {
		return new int[0][];
	}

	public void setCpuPinMap(int[][] newPinMap) {
		throw new NotImplementedException();
	}

	public void save(String s) {

	}

	protected String _vmName = null;

	public String getVmClass() {
		return null;
	}

	public void setVmClass(String className) {

	}

	public String getVmName()
	{
		return _vmName;
	}

	public void setVmName(String newVmName)
	{
		_vmName = newVmName;
	}

	protected String _configurationSourceUrl = null;
	public String getConfigurationUri()
	{
		return _configurationSourceUrl;
	}

	protected boolean _canHotAddCpus = false;
	public boolean canHotAddCpus() { return _canHotAddCpus; }
	protected int _cpuCount;
	public int getCpuCount()
	{
		return _cpuCount;
	}
	public void setCpuCount(int numberOfCpus)
	{
		if(this.getRuntimeState() == VMRuntimeState.Off || this.getRuntimeState() == VMRuntimeState.ConfigurationInvalid || this.getRuntimeState() == VMRuntimeState.Initializing) {
			_cpuCount = numberOfCpus;
		}
	}
	// -m size[K|k|M|m|G|g|T|t]


	protected boolean _canHotAddRam = false;
	public boolean canHotAddRam()
	{
		return _canHotAddRam;
	}
	protected long _memorySize;	// in bytes, HVM may not support this granularity and will complain or adjust on its own if not properly aligned on a block boundry
	public long getMemory()
	{
		return _memorySize;
	}
	public void setMemory(long sizeInMegabytes)
	{
		if(this.getRuntimeState() == VMRuntimeState.Off || this.getRuntimeState() == VMRuntimeState.ConfigurationInvalid || this.getRuntimeState() == VMRuntimeState.Initializing) {
			_memorySize = sizeInMegabytes;
		}
	}


	protected boolean _wireGuestMemory = false;
	public boolean getWireGuestMemory() {
		return _wireGuestMemory;
	}
	public void setWireGuestMemory(boolean wireGuestMemory) {
		_wireGuestMemory = wireGuestMemory;
	}

	protected boolean _rtcIsUTC = false;
	public boolean getRTCisUTC() {
		return _rtcIsUTC;
	}
	public void setRTCisUTC(boolean isUTC) {
		_rtcIsUTC = isUTC;
	}

	void load(String configurationFileURL)
	{

	}

	public static void loadFile(File url, IVMState bvmState, IHypervisor hvm) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error(e);
			return;
		}

		org.w3c.dom.Document doc = null;
		try {
			doc = builder.parse(url);
		} catch (SAXException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
			return;
		} catch (IOException e) {
			logger.error(e);
			return;
		}
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = null;
		try {
			expr = xpath.compile("//VirtualMachine/@name");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		String vmName = "";
		try {
			vmName = (String) expr.evaluate(doc, XPathConstants.STRING);
			logger.debug("vmName: " + vmName);

		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		bvmState.setVmName(vmName);

		try {
			expr = xpath.compile("//VirtualMachine/@osClass");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		String osClass = "none";
		try {
			osClass = (String) expr.evaluate(doc, XPathConstants.STRING);
			if(osClass == null) {
				osClass = "";
			}
			if(osClass.isEmpty()) {
				// The default for now
				osClass = "uefi";
				logger.debug("osClass: " + osClass + " (default)");
			} else {
				logger.debug("osClass: " + osClass);
			}
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		bvmState.setVmClass(osClass);

		try {
			expr = xpath.compile("//VirtualMachine/UUID");
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		UUID newUuid = UUID.randomUUID();
		try {
			newUuid = UUID.fromString((String) expr.evaluate(doc, XPathConstants.STRING));
			logger.debug("uuid: " + newUuid.toString());
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		bvmState.setUuid(newUuid);

		try {
			expr = xpath.compile("//VirtualMachine/cpus/@count");
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		int cpuCount = 1;
		try {
			cpuCount = Integer.parseInt((String) expr.evaluate(doc, XPathConstants.STRING));
			logger.debug("cpuCount: " + cpuCount);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		bvmState.setCpuCount(cpuCount);

		try {
			expr = xpath.compile("//VirtualMachine/cpus/@hotAddEnabled");
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		boolean hotAddEnabled = false;
		try {
			hotAddEnabled = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
			logger.debug("hotAddCpuEnabled: " + hotAddEnabled);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		try {
			expr = xpath.compile("//VirtualMachine/memory/@size");
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		long memorySize = 1;
		try {
			memorySize = Long.parseLong((String) expr.evaluate(doc, XPathConstants.STRING));
			logger.debug("memorySize: " + memorySize);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		// Adjust to megabytes, thats what our API expects currently
		bvmState.setMemory(memorySize);

		try {
			expr = xpath.compile("//VirtualMachine/memory/@hotAddEnabled");
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		hotAddEnabled = false;
		try {
			hotAddEnabled = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
			logger.debug("hotAddMemoryEnabled: " + hotAddEnabled);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		try {
			expr = xpath.compile("//VirtualMachine/RTC/@isUTC");
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		boolean isUTC = false;
		try {
			isUTC = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
			logger.debug("RTCisUTC: " + isUTC);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		bvmState.setRTCisUTC(isUTC);


		try {
			expr = xpath.compile("//VirtualMachine/Devices/child::*");
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		NodeList deviceNodes = null;
		try {
			deviceNodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			Node devNode;
			int devLength = deviceNodes.getLength();
			IVMDevice tmpDevice;
			logger.debug("Devices Configured: " + devLength);

			for(int i = 0; i < devLength; i++) {
				devNode = deviceNodes.item(i);
				IVMDevice hvmDevice;
				int bus = Integer.parseInt((String)xpath.evaluate("@bus", devNode, XPathConstants.STRING));
				int slot = Integer.parseInt((String)xpath.evaluate("@slot", devNode, XPathConstants.STRING));
				int function = Integer.parseInt((String)xpath.evaluate("@function", devNode, XPathConstants.STRING));
				try {
					hvmDevice = hvm.parseDevice(devNode);
					hvmDevice.configureDevice(bus, slot);
					logger.info("PCI device: " + hvmDevice.getDeviceAddress() + " " + hvmDevice.toString().replace("us.wimsey.apiary.apiaryd.virtualmachines.devices.", ""));
				} catch(IllegalArgumentException ex) {
					logger.error("PCI device: " + bus + ":" + slot + ":" + function + " Error parsing device configuration: " + ex.getMessage());
				}
			}
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}



		/*
		public boolean getGenerateAPICTables();		// -A
		public boolean getDumpGetMemWithCore();		// -C
		public boolean getHVMExitOnUnemulatedIOPort();	// -e
		public short getGDBPort();					// -g <port>
		public boolean getYieldCPUOnHLT();			// -H (If false, vm will use 100% CPU)
		public boolean getHVMExitOnPAUSE();			// -P
		public boolean getWireGuestMemory();		// -S
		public boolean getRTCisUTC();				// -u
		*/

	}
}
