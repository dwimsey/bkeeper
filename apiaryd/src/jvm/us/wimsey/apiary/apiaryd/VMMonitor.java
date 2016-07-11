package us.wimsey.apiary.apiaryd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.hypervisors.HypervisorFactory;
import us.wimsey.apiary.apiaryd.hypervisors.IHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 3/31/16.
 */
public class VMMonitor {
	private static final Logger logger = LogManager.getLogger(VMMonitor.class);

	List<IVMState> virtualMachines = new ArrayList<IVMState>();

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
				logger.warn("Exercising apiary shutdown manager hook.");
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

		String vmRegistryPath = "/usr/local/apiary/virtualmachines";
		if(apiaryProperties.containsKey("apiaryd.vmregistry.path") == true) {
			vmRegistryPath = apiaryProperties.getProperty("apiaryd.vmregistry.path");
		}
		File dir = new File(vmRegistryPath);
		File [] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// the toLowerCase() means we don't care about the case of the extention effectively, when matching.
				return name.toLowerCase().endsWith(".vmxml");
			}
		});

		for (File xmlfile : files) {
			// Load up the registered VMs from their config files and add them to the VM list.
			virtualMachines.add(localHypervisor.registerVm(xmlfile));
		}

		/*
		// Attempt to cleanup with any apiary VMs that may have been left running because apiaryd crashed
		List<String> vms = localHypervisor.getVMList();
		vms.forEach((vm)-> {
			logger.warn("Existing VM encountered: " + vm);
			if(vm.startsWith("apiary_") == true) {
				logger.warn("Found apiary vm, checking state ...");

			}
		});
		*/
	}

	public void shutdown(Boolean killRemainingVMs) {
	}

	public IVMState register(String s) {
		IVMState newVm = null;


		//newVm = create(vmTemplateName);

		if(newVm != null) {
			virtualMachines.add(newVm);
		}
		return newVm;
	}

	public IVMState create(String vmTemplateName) {
		IVMState newVm = null;
		newVm = localHypervisor.create(vmTemplateName);
		return newVm;
	}
}
