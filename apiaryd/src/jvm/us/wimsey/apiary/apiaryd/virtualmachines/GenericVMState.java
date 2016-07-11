package us.wimsey.apiary.apiaryd.virtualmachines;

import java.util.UUID;

/**
 * Created by dwimsey on 7/10/16.
 */
public abstract class GenericVMState implements IVMState {
	final private UUID _SMBUUID;
	protected GenericVMState()
	{
		_SMBUUID = UUID.randomUUID();
	}

	@Override
	public UUID getUUID() {
		return _SMBUUID;
	}

	private boolean _canHotAddCpus = false;
	public boolean canHotAddCpus()
	{
		return _canHotAddCpus;
	}

	private boolean _canHotAddMemory = false;
	public boolean canHotAddMemory()
	{
		return _canHotAddMemory;
	}

	private String _vmName = null;
	public String getVMName()
	{
		return _vmName;
	}
	String _configurationSourceUrl = null;
	public String getConfigurationUri()
	{
		return _configurationSourceUrl;
	}

	private int _cpuCount;
	public int getCpuCount()
	{
		return _cpuCount;
	}
	public void setCpuCount(int numberOfCpus)
	{
		_cpuCount = numberOfCpus;
	}
	// -m size[K|k|M|m|G|g|T|t]

	int _memorySize;	// in bytes, HVM may not support this granularity and will complain or adjust on its own if not properly aligned on a block boundry
	public int getRam()
	{
		return _memorySize / (1024*1024);
	}

	public void setRam(int sizeInMegabytes)
	{
		_memorySize = sizeInMegabytes * (1024 * 1024);
	}

	void load(String configurationFileURL)
	{

	}
}
