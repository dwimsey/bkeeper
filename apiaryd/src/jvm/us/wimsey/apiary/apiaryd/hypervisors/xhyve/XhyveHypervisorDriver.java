package us.wimsey.apiary.apiaryd.hypervisors.xhyve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.hypervisors.GenericHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;
import us.wimsey.apiary.apiaryd.virtualmachines.VMStateBase;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.factories.LPCBridgeFactory;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.factories.PCIHostbridgeFactory;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class XhyveHypervisorDriver extends GenericHypervisor {
	private static final Logger logger = LogManager.getLogger(XhyveHypervisorDriver.class);

	private String sshShellCmd = "ssh ${SSH_OPTIONS} ${REMOTE} ${SHELLCMD}";

	public XhyveHypervisorDriver(Properties hypervisorInitializationProperties)
	{
		super(hypervisorInitializationProperties);
		if(_props.containsKey("apiaryd.hypervisor.ConnectionString") == true) {
			String connectionString = _props.getProperty("apiaryd.hypervisor.ConnectionString");
			if(connectionString.startsWith("ssh:") == true) {
				// This is a remote hypervisor (ONLY FOR TESTING!@$!@$!@$!@$
				// Patch up the shell command line to make it work as expected

				String sshUri = connectionString.substring(4);
				shellCmd = sshShellCmd.replace("${REMOTE}", sshUri).replace("${SSH_OPTIONS}", "");
			}
		}

		deviceFactories.put("hostbridge", new PCIHostbridgeFactory(this));
		deviceFactories.put("lpc", new LPCBridgeFactory(this));
	}

	@Override
	public IVMState registerVm(File URL) {
		XhyveVMState vmState = new XhyveVMState();
		VMStateBase.loadFile(URL, vmState, this);
		return vmState;
	}

	@Override
	public List<String> getVMList() {
		return null;
	}

	@Override
	public IVMState create(String vmTemplateName) {
		return null;
	}
}
