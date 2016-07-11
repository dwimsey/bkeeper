package us.wimsey.apiary.apiaryd.hypervisors.bhyve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.virtualmachines.GenericVMState;

/**
 * Created by dwimsey on 7/10/16.
 */
public class BhyveVMState extends GenericVMState {
	private static final Logger logger = LogManager.getLogger(BhyveVMState.class);

	private VMRuntimeState _runtimeState = VMRuntimeState.Initializing;

	public BhyveVMState() {
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

	@Override
	public boolean getYieldCPUOnHLT() {
		return true;
	}

	@Override
	public boolean getHVMExitOnPAUSE() {
		return false;
	}

	@Override
	public void powerOn() {
		if(_runtimeState != VMRuntimeState.Off) {
			if(_runtimeState == VMRuntimeState.Running) {
				throw new IllegalStateException("Can not power on virtual machine that is not off: " + _runtimeState.toString());
			} else if(_runtimeState == VMRuntimeState.ConfigurationInvalid) {
				throw new IllegalStateException("Can not power on virtual machine with invalid configuration: " + _runtimeState.toString());
			}
		}
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
