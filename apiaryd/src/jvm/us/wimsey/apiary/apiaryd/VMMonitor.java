package us.wimsey.apiary.apiaryd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.hypervisors.HypervisorFactory;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;

import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 3/31/16.
 */
public class VMMonitor {
	private static final Logger logger = LogManager.getLogger(VMMonitor.class);

	List<VirtualMachineRunner> virtualMachines = null;

	private Properties apiaryProperties;
	private float shutdownGracePeriod;
	public VMMonitor(Properties props) {
		apiaryProperties = props;
		if(apiaryProperties.containsKey("apiaryd.hypervisor.shutdown.graceperiod") == true) {
			shutdownGracePeriod = java.lang.Float.parseFloat(apiaryProperties.getProperty("apiaryd.hypervisor.shutdown.graceperiod"));
		} else {
			// excessively safe, but wont allow a completely bricked server if something hangs up
			shutdownGracePeriod = 3600.0f;
		}
	}

	private IHypervisor localHypervisor = null;
	public void start() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if(localHypervisor != null) {
					localHypervisor.shutdown(shutdownGracePeriod);
				}
			}
		});



		try {
			localHypervisor = HypervisorFactory.getLocalHypervisor(apiaryProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Attempt to reconnect with any apiary VMs that may have been left running because apiaryd crashed
		List<String> vms = localHypervisor.getVMList();
		vms.forEach((vm)-> {
			logger.warn("Existing VM encountered: " + vm);
			if(vm.startsWith("apiary_") == true) {
				logger.warn("Found apiary vm, checking state ...");
			}
		});
	}

	public void shutdown(Boolean killRemainingVMs) {
	}
}
