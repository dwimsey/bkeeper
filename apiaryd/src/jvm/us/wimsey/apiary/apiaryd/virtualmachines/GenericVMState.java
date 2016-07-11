package us.wimsey.apiary.apiaryd.virtualmachines;

import java.util.UUID;

/**
 * Created by dwimsey on 7/10/16.
 */
public abstract class GenericVMState implements IVMState {
	final private UUID _SMBUUID;
	private VMRuntimeState _runtimeState;
	protected GenericVMState(String newVMName)
	{
		_SMBUUID = UUID.randomUUID();
		// Until more is done, the VM configuration is invalid, this is okay for now, not
		// really an error just a statement of fact.
		_runtimeState = VMRuntimeState.Initializing;
		_vmName = newVMName;

		validateConfig();
	}

	private void validateConfig()
	{

		if(_runtimeState == VMRuntimeState.Initializing) {
			_runtimeState = VMRuntimeState.Off;
		}
	}
	@Override
	public UUID getUUID() {
		return _SMBUUID;
	}

	@Override
	public boolean getIgnoreMissingMSRs() {
		return false;
	}

	@Override
	public boolean getForceVirtIOToUseMSI() {
		return false;
	}

	@Override
	public boolean getAPICx2Mode() {
		return false;
	}

	@Override
	public boolean getMPTableDisabled() {
		return false;
	}

	@Override
	public int[][] getCpuPinMap() {
		return new int[0][];
	}

	@Override
	public void setCpuPinMap(int[][] newPinMap) {

	}

	@Override
	public void powerOn() {

	}

	@Override
	public void powerOff(float gracePeriod) {

	}

	@Override
	public void reset() {

	}

	@Override
	public VMRuntimeState getRuntimeState() {
		return null;
	}

	@Override
	public void save(String s) {

	}

	private String _vmName = null;

	@Override
	public String getVmClass() {
		return null;
	}

	@Override
	public void setVmClass(String className) {

	}

	public String getVMName()
	{
		return _vmName;
	}

	String _configurationSourceUrl = null;
	public String getConfigurationUri()
	{
		return _configurationSourceUrl;
	}

	private boolean _canHotAddCpus = false;
	public boolean canHotAddCpus() { return _canHotAddCpus; }
	private int _cpuCount;
	public int getCpuCount()
	{
		return _cpuCount;
	}
	public void setCpuCount(int numberOfCpus)
	{

		if(_runtimeState == VMRuntimeState.Off || this.getRuntimeState() == VMRuntimeState.ConfigurationInvalid || this.getRuntimeState() == VMRuntimeState.Initializing) {
			_cpuCount = numberOfCpus;
		}
	}
	// -m size[K|k|M|m|G|g|T|t]


	private boolean _canHotAddRam = false;
	public boolean canHotAddRam()
	{
		return _canHotAddRam;
	}
	int _memorySize;	// in bytes, HVM may not support this granularity and will complain or adjust on its own if not properly aligned on a block boundry
	public int getRam()
	{
		return _memorySize / (1024*1024);
	}
	public void setRam(int sizeInMegabytes)
	{
		_memorySize = sizeInMegabytes * (1024 * 1024);
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
		return false;
	}

	@Override
	public boolean getHVMExitOnPAUSE() {
		return false;
	}

	@Override
	public boolean getWireGuestMemory() {
		return false;
	}

	@Override
	public boolean getRTCisUTC() {
		return false;
	}

	void load(String configurationFileURL)
	{

	}
}
