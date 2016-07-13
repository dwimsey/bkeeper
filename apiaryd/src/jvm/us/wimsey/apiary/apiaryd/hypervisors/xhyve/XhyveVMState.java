package us.wimsey.apiary.apiaryd.hypervisors.xhyve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.virtualmachines.VMStateBase;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;

/**
 * Created by dwimsey on 7/10/16.
 */
public class XhyveVMState extends VMStateBase {
	private static final Logger logger = LogManager.getLogger(XhyveVMState.class);

	private VMRuntimeState _runtimeState = VMRuntimeState.Initializing;

	public XhyveVMState() {
		super();
	}

	@Override
	public boolean getGenerateAPICTables() {
		return false;
	}

	@Override
	public boolean getDumpGetMemWithCore() {
		return false;
	}

	@Override
	public boolean getHVMExitOnUnemulatedIOPort() {
		return false;
	}

	@Override
	public short getGDBPort() {
		return 0;
	}

	private boolean _ignoreMissingMSRs = true;
	@Override
	public boolean getIgnoreMissingMSRs() { return _ignoreMissingMSRs; }

	private boolean _yieldOnHlt = true;
	@Override
	public boolean getYieldCPUOnHLT() {
		return _yieldOnHlt;
	}

	@Override
	public boolean getHVMExitOnPAUSE() {
		return false;
	}

	@Override
	public void powerOn() {
		_runtimeState = VMRuntimeState.Off;
		if(_runtimeState != VMRuntimeState.Off) {
			if(_runtimeState == VMRuntimeState.ConfigurationInvalid) {
				throw new IllegalStateException("Can not power on virtual machine with invalid configuration: " + _runtimeState.toString());
			}
			throw new IllegalStateException("Can not power on virtual machine that is not off: " + _runtimeState.toString());
		}

		String cmdline = "/Users/dwimsey/bin/xhyve";

		cmdline += " -c " + _cpuCount;
		cmdline += " -m " + _memorySize / (1024*1024);
		if(_SMBUUID != null) {
			cmdline += " -U " + _SMBUUID.toString();
		}
		cmdline += (_rtcIsUTC ? " -u" : "") + (_wireGuestMemory ? " -W" : "") + (_yieldOnHlt ? " -H" : "") + (_ignoreMissingMSRs ? " -w" : "");

		for(IVMDevice pciDevice : getDeviceList()) {
			cmdline += pciDevice.getCmdline();
		}

/*
		-s 0,hostbridge \
		-s 3,ahci-hd,/dev/zvol/zfs01/vm-nfs-01/bhyve/vdc-02.wimsey.us/disk01  \
		-s 10,virtio-net,tap0 \
		-s 31,lpc \
		-l com1,/dev/nmdm0A \
		-l com2,/dev/nmdm1A \
		-l bootrom,/apiary/system/efi/BHYVE_UEFI_20151002.fd \
*/
		cmdline += " " + this.getVmName();
		logger.info("Powering on VM: " + cmdline);
	}

	@Override
	public void powerOff(float gracePeriod) {
		if(_runtimeState != VMRuntimeState.Running) {
			throw new IllegalStateException("Can not power off virtual machine that is not running: " + _runtimeState.toString());
		}
	}

	@Override
	public void reset() {
		if(_runtimeState != VMRuntimeState.Running) {
			throw new IllegalStateException("Can not reset virtual machine that is not running: " + _runtimeState.toString());
		}
	}

	@Override
	public VMRuntimeState getRuntimeState() {
		return _runtimeState;
	}

	@Override
	public void validateConfiguration() {
		//if(this.getCpuCount())
	}
}
