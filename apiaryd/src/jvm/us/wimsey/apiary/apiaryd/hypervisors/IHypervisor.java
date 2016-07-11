package us.wimsey.apiary.apiaryd.hypervisors;

import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;

import java.io.File;
import java.util.List;

/**
 * Created by dwimsey on 7/9/16.
 */
public interface IHypervisor {
	// Create a IVMState object from a VM configuration file
	public IVMState registerVm(String URL);
	public IVMState registerVm(File xmlfile);

    public String getHypervisorName();
	public List<String> getVMList();

	public void shutdown(float gracePeriod);

	IVMState create(String vmTemplateName);
}
