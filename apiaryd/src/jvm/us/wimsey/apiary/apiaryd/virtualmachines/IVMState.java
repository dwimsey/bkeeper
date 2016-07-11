package us.wimsey.apiary.apiaryd.virtualmachines;

import java.util.UUID;

/**
 * Created by dwimsey on 7/10/16.
 */
public interface IVMState {
	public String getVmClass();
	public void setVmClass(String className);

	public String getVmName();

	void setVmName(String newVmName);

	public String getConfigurationUri();

	public boolean canHotAddCpus();
	public int getCpuCount();					// -c 2
	public void setCpuCount(int numberOfCpus);
	public boolean canHotAddRam();
	public long getMemory();						// -m size[K|k|M|m|G|g|T|t]
	public void setMemory(long sizeInBytes);

	public boolean getGenerateAPICTables();		// -A
	public boolean getDumpGetMemWithCore();		// -C
	public boolean getHVMExitOnUnemulatedIOPort();	// -e
	public short getGDBPort();					// -g <port>
	public boolean getYieldCPUOnHLT();			// -H (If false, vm will use 100% CPU)
	public boolean getHVMExitOnPAUSE();			// -P
	public boolean getWireGuestMemory();		// -S
	public boolean getRTCisUTC();				// -u
	public void setRTCisUTC(boolean isUTC);		// -u
	public UUID getUuid();						// -U <UUID>
	void setUuid(UUID newUuid);					// -U <UUID>
	public boolean getIgnoreMissingMSRs();		// -w (Required for Windows currently)
	public boolean getForceVirtIOToUseMSI();	// -W
	public boolean getAPICx2Mode();				// -x
	public boolean getMPTableDisabled();		// -y

	public int[][] getCpuPinMap();		// -p vcpu:hostcpu
	public void setCpuPinMap(int[][] newPinMap);	// -1 means not pinned

	public void powerOn();
	public void powerOff(float gracePeriod);
	public void reset();
	public VMRuntimeState getRuntimeState();

	void save(String s);
	void validateConfiguration();
	public enum VMRuntimeState {
		Initializing(0),
		Off (10),
		//Starting (20),
		Running (30),
		//Suspending (40),
		//Suspended (50),
		//Stopping (60),
		ConfigurationInvalid(98),
		Zombie(99);

		final private int runtimeState;
		VMRuntimeState(int _runtimeState)
		{
			runtimeState = _runtimeState;
		}
	}
}
