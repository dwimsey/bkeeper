package us.wimsey.apiary.apiaryd;

import us.wimsey.apiary.apiaryd.hypervisors.HypervisorFactory;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;

import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 3/31/16.
 */
public class VMMonitor {
	List<VirtualMachineRunner> virtualMachines = null;

	private Properties apiaryProperties;
	public VMMonitor(Properties props) {
		apiaryProperties = props;
	}

	private IHypervisor localHypervisor = null;
	public void start() {
		try {
			localHypervisor = HypervisorFactory.getLocalHypervisor(apiaryProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown(Boolean killRemainingVMs) {
	}
}
