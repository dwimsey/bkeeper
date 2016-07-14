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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dwimsey on 7/10/16.
 */
public abstract class VMStateBase implements IVMState {
	private static final Logger logger = LogManager.getLogger(VMStateBase.class);

	protected IVMState.VMRuntimeState _runtimeState = IVMState.VMRuntimeState.Off;

	protected UUID _SMBUUID;
	protected VMStateBase()
	{
		_SMBUUID = UUID.randomUUID();
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

	protected String _vmName = null;

	synchronized public String getVmClass() {
		return null;
	}

	synchronized public void setVmClass(String className) {

	}

	synchronized public String getVmName()
	{
		return _vmName;
	}

	synchronized public void setVmName(String newVmName)
	{
		_vmName = newVmName;
	}

	protected String _configurationSourceUrl = null;
	synchronized public String getConfigurationUri()
	{
		return _configurationSourceUrl;
	}

	protected boolean _canHotAddCpus = false;
	synchronized public boolean canHotAddCpus() { return _canHotAddCpus; }
	protected int _cpuCount;
	synchronized public int getCpuCount()
	{
		return _cpuCount;
	}
	synchronized public void setCpuCount(int numberOfCpus)
	{
		if(this.getRuntimeState() == VMRuntimeState.Off || this.getRuntimeState() == VMRuntimeState.ConfigurationInvalid || this.getRuntimeState() == VMRuntimeState.Initializing) {
			_cpuCount = numberOfCpus;
		}
	}
	// -m size[K|k|M|m|G|g|T|t]


	protected boolean _canHotAddRam = false;
	synchronized public boolean canHotAddRam()
	{
		return _canHotAddRam;
	}
	protected long _memorySize;	// in bytes, HVM may not support this granularity and will complain or adjust on its own if not properly aligned on a block boundry
	synchronized public long getMemory()
	{
		return _memorySize;
	}
	synchronized public void setMemory(long sizeInMegabytes)
	{
		if(this.getRuntimeState() == VMRuntimeState.Off || this.getRuntimeState() == VMRuntimeState.ConfigurationInvalid || this.getRuntimeState() == VMRuntimeState.Initializing) {
			_memorySize = sizeInMegabytes;
		}
	}


	protected boolean _wireGuestMemory = false;
	synchronized public boolean getWireGuestMemory() {
		return _wireGuestMemory;
	}
	synchronized public void setWireGuestMemory(boolean wireGuestMemory) {
		_wireGuestMemory = wireGuestMemory;
	}

	protected boolean _rtcIsUTC = false;
	synchronized public boolean getRTCisUTC() {
		return _rtcIsUTC;
	}
	synchronized public void setRTCisUTC(boolean isUTC) {
		_rtcIsUTC = isUTC;
	}


	public synchronized void loadFile(File url, IHypervisor hvm) {
		if(_runtimeState == VMRuntimeState.Running || _runtimeState == VMRuntimeState.Stopping || _runtimeState == VMRuntimeState.Initializing) {

		}

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

		_runtimeState = VMRuntimeState.ConfigurationInvalid; // We're reloading the configuration currently


		String vmName = "";
		try {
			vmName = (String) xpath.evaluate("//VirtualMachine/@name", doc, XPathConstants.STRING);
			logger.debug("vmName: " + vmName);

		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		_vmName = vmName.toString();

		String osClass = "none";
		try {
			osClass = (String) xpath.evaluate("//VirtualMachine/@osClass", doc, XPathConstants.STRING);
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

		this.setVmClass(osClass);

		UUID newUuid = UUID.randomUUID();
		try {
			newUuid = UUID.fromString((String) xpath.evaluate("//VirtualMachine/UUID", doc, XPathConstants.STRING));
			logger.debug("uuid: " + newUuid.toString());
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		this.setUuid(newUuid);

		int cpuCount = 1;
		try {
			cpuCount = Integer.parseInt((String) xpath.evaluate("//VirtualMachine/cpus/@count", doc, XPathConstants.STRING));
			logger.debug("cpuCount: " + cpuCount);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		this.setCpuCount(cpuCount);

		boolean hotAddEnabled = false;
		try {
			hotAddEnabled = Boolean.parseBoolean((String) xpath.evaluate("//VirtualMachine/cpus/@hotAddEnabled", doc, XPathConstants.STRING));
			logger.debug("hotAddCpuEnabled: " + hotAddEnabled);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		long memorySize = 1;
		try {
			memorySize = Long.parseLong((String) xpath.evaluate("//VirtualMachine/memory/@size", doc, XPathConstants.STRING));
			logger.debug("memorySize: " + memorySize);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		// Adjust to megabytes, thats what our API expects currently
		this.setMemory(memorySize);

		hotAddEnabled = false;
		try {
			hotAddEnabled = Boolean.parseBoolean((String) xpath.evaluate("//VirtualMachine/memory/@hotAddEnabled", doc, XPathConstants.STRING));
			logger.debug("hotAddMemoryEnabled: " + hotAddEnabled);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}

		boolean isUTC = false;
		try {
			isUTC = Boolean.parseBoolean((String) xpath.evaluate("//VirtualMachine/RTC/@isUTC", doc, XPathConstants.STRING));
			logger.debug("RTCisUTC: " + isUTC);
		} catch (XPathExpressionException e) {
			logger.error(e);
			//bvmState.setRuntimeState(VMRuntimeState.ConfigurationInvalid);
		}
		this.setRTCisUTC(isUTC);

		NodeList deviceNodes = null;
		try {
			deviceNodes = (NodeList) xpath.evaluate("//VirtualMachine/Devices/child::*", doc, XPathConstants.NODESET);
			Node devNode;
			int devLength = deviceNodes.getLength();
			IVMDevice tmpDevice;
			logger.debug("Devices Configured: " + devLength);

			for(int i = 0; i < devLength; i++) {
				devNode = deviceNodes.item(i);
				IVMDevice hvmDevice;
				try {
					hvmDevice = hvm.parseDevice(devNode);
					this.addDevice(hvmDevice);
					logger.info("PCI device: " + hvmDevice.getDeviceAddress() + " " + hvmDevice.toString().replace("us.wimsey.apiary.apiaryd.virtualmachines.devices.", ""));
				} catch(IllegalArgumentException ex) {
					logger.error("PCI device: Error parsing device configuration: " + ex.getMessage());
				}
			}
		} catch (XPathExpressionException e) {
			logger.error(e);
		}

		if(this.validateConfiguration() == true) {
			this._runtimeState = VMRuntimeState.Off;
		}
	}


	private List<IVMDevice> _deviceList = new LinkedList<IVMDevice>();
	synchronized public List<IVMDevice> getDeviceList()
	{
		return _deviceList;
	}

	synchronized public void addDevice(IVMDevice vmDevice)
	{
		_deviceList.add(vmDevice);
	}

	synchronized public void removeDevice(IVMDevice vmDevice)
	{
		_deviceList.remove(vmDevice);
	}

}
