package us.wimsey.apiary.apiaryd.hypervisors.bhyve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.virtualmachines.VMStateBase;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.IVMDevice;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by dwimsey on 7/10/16.
 */
public class BhyveVMState extends VMStateBase {
	private static final Logger logger = LogManager.getLogger(BhyveVMState.class);

	private String _cmdBase;

	public BhyveVMState() {
		super();
		String osName = System.getProperty("os.name");
		switch (osName) {
			case "Mac OS X":
				_cmdBase = "/Users/dwimsey/bin/xhyve";
				break;
			case "FreeBSD":
				_cmdBase = "/usr/sbin/bhyve";
				break;
			default:
				// make a guess
				_cmdBase = "/usr/bin/env bhyve";
				break;
		}
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

	Process _vmProcess = null;
	@Override
	public void powerOn() {
		if(_runtimeState != VMRuntimeState.Off && _runtimeState != VMRuntimeState.Zombie) {
			if(_runtimeState == VMRuntimeState.ConfigurationInvalid) {
				throw new IllegalStateException("Can not power on virtual machine with invalid configuration: " + _runtimeState.toString());
			}
			throw new IllegalStateException("Can not power on virtual machine that is not off: " + _runtimeState.toString());
		}

		String cmdline = _cmdBase;

		boolean validConfiguration = true;
		cmdline += " -c " + _cpuCount;
		cmdline += " -m " + _memorySize / (1024*1024);
		if(_SMBUUID != null) {
			cmdline += " -U " + _SMBUUID.toString();
		}
		cmdline += (_rtcIsUTC ? " -u" : "") + (_wireGuestMemory ? " -W" : "") + (_yieldOnHlt ? " -H" : "") + (_ignoreMissingMSRs ? " -w" : "");

		for(IVMDevice pciDevice : getDeviceList()) {
			if("0:0:0".equals(pciDevice.getDeviceAddress().toString()) == true) {
				if(pciDevice.getClass().getSimpleName().equals("PCIHostBridge") == false) {
					// this is invalid, hostbridge must be on 0:0:0
					logger.error("Could not configure device, only hostbridge is allowed on 0:0:0: " + pciDevice.getDeviceName());
					validConfiguration = false;
				}
			} else if(pciDevice.getClass().getSimpleName().equals("hostbridge") == true) {
				if("0:0:0".equals(pciDevice.getDeviceAddress().toString()) == false) {
					logger.error("Could not configure device, hostbridge must be on 0:0:0: " + pciDevice.getDeviceName());
					validConfiguration = false;
				}
			} else if(pciDevice.getClass().getSimpleName().equals("lpc") == true) {
				if(pciDevice.getDeviceAddress().startsWith("0:31:") == false) {
					logger.error("Could not configure device, lpc must be on bus 0, slot 31 due to a bug in some UEFI firmware versions: " + pciDevice.getDeviceName());
					validConfiguration = false;
				}
			}

			cmdline += pciDevice.getCmdline();
		}

		cmdline += " " + this.getVmName();
		if(validConfiguration == false) {
			logger.error("VM Configuration is invalid, can not power on.");
			return;
		}

		logger.info("Powering on VM: " + cmdline);
		try {
			_vmProcess = Runtime.getRuntime().exec(cmdline);
		} catch (IOException e) {
			logger.error("Could not start vm process: " + e.getMessage(), e);
			_vmProcess = null;
			return;
		}

		if(_vmProcess.isAlive() == true) {
			_runtimeState = VMRuntimeState.Running;
		}
	}

	@Override
	public void powerOff(float gracePeriod) {
		logger.info("Powering off VM: " + _vmName);

		if(_runtimeState != VMRuntimeState.Running && _runtimeState != VMRuntimeState.Initializing && _runtimeState != VMRuntimeState.Stopping && _runtimeState != VMRuntimeState.Zombie) {
			throw new IllegalStateException("Can not power off virtual machine that is not running: " + _runtimeState.toString());
		}

		this._runtimeState = VMRuntimeState.Stopping;
		if(_vmProcess != null) {
			if(gracePeriod < 0.01) {
				// kill it
				if(_vmProcess.isAlive() == true) {
					_vmProcess.destroyForcibly();
				}
			} else {
				try {
					_vmProcess.waitFor(((int)(gracePeriod * 1000)), TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(_vmProcess.isAlive() == true) {
					_vmProcess.destroyForcibly();
				}
			}
		}
	}

	@Override
	public void reset() {
		if(_runtimeState != VMRuntimeState.Running && _runtimeState != VMRuntimeState.Initializing && _runtimeState != VMRuntimeState.Stopping) {
			throw new IllegalStateException("Can not reset virtual machine that is not running: " + _runtimeState.toString());
		}
	}

	@Override
	public VMRuntimeState getRuntimeState() {
		return _runtimeState;
	}

	@Override
	public boolean validateConfiguration() {
		return true;
	}
}
