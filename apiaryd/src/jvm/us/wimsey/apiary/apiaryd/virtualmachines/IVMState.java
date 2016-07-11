package us.wimsey.apiary.apiaryd.virtualmachines;

import java.util.UUID;

/**
 * Created by dwimsey on 7/10/16.
 */
public interface IVMState {
	public String getVMName();
	public String getConfigurationUri();

	public int getCpuCount();					// -c 2
	public void setCpuCount(int numberOfCpus);
	public int getRam();						// -m size[K|k|M|m|G|g|T|t]
	public void setRam(int sizeInMegabytes);

	public boolean getGenerateAPICTables();		// -A
	public boolean getDumpGetMemWithCore();		// -C
	public boolean getHVMExitOnUnemulatedIOPort();	// -e
	public short getGDBPort();					// -g <port>
	public boolean getYieldCPUOnHLT();			// -H (If false, vm will use 100% CPU)
	public boolean getHVMExitOnPAUSE();			// -P
	public boolean getWireGuestMemory();		// -S
	public boolean getRTCisUTC();				// -u
	public UUID getUUID();					// -U <UUID>
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

	public enum VMRuntimeState {
		Off (0),
		Starting (1),
		Stopping (2),
		ConfigurationInvalid(98),
		Crashed(99);

		final private int runtimeState;
		VMRuntimeState(int _runtimeState)
		{
			runtimeState = _runtimeState;
		}
	}
}
