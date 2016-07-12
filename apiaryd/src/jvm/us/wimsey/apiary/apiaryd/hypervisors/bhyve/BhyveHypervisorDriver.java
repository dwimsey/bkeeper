package us.wimsey.apiary.apiaryd.hypervisors.bhyve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.VirtualMachineRunner;
import us.wimsey.apiary.apiaryd.hypervisors.GenericHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.VMStateBase;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.factories.LPCBridgeFactory;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.factories.PCIHostbridgeFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class BhyveHypervisorDriver extends GenericHypervisor {
	private static final Logger logger = LogManager.getLogger(BhyveHypervisorDriver.class);

	public static IVMState createVm(String vmName)
	{
		return null;
	}

	public BhyveHypervisorDriver(Properties hypervisorInitializationProperties)
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
//		deviceFactories.put("ahci-hd", new PCIAHCIHDFactory());
//		deviceFactories.put("virtio-net", new PCIVirtIONetFactory());
//		deviceFactories.put("virtio-blk", new PCIVirtIOBlockFactory());
//		deviceFactories.put("virtio-rnd", new PCIVirtIORandomFactory());
	}

	private String sshShellCmd = "ssh ${SSH_OPTIONS} ${REMOTE} ${SHELLCMD}";

	public List<String> getVMList()
	{
		List<String> vmList = new ArrayList<String>();

		Process cmdProcess = null;
		String vmListExecCmd = shellCmd.replace("${SHELLCMD}", "ls /dev/vmm");
		try {
			cmdProcess = Runtime.getRuntime().exec(vmListExecCmd);
			int exitCode = -1;
			try {
				exitCode = cmdProcess.waitFor();
				if(exitCode != 0) {
					System.err.println("Could not get vm list: " + exitCode);
				} else {
					BufferedReader in = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
					String line;
					try {
						while ((line = in.readLine()) != null) {
							vmList.add(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return vmList;
	}

	@Override
	public IVMState create(String vmTemplateName) {
		if(vmTemplateName == null) {
			throw new IllegalArgumentException("vmTemplateName can not be NULL.");
		}

		vmTemplateName = vmTemplateName.trim();
		if(vmTemplateName.isEmpty() == true) {
			throw new IllegalArgumentException("vmTemplateName can not be empty.");
		}

		IVMState newVm = null;

		return newVm;
	}

	public void startVM(VirtualMachineRunner vmRunner)
	{

	}

	@Override
	public IVMState registerVm(String URL) {
		return registerVm(new File(URL));
	}

	@Override
	public IVMState registerVm(File URL) {
		BhyveVMState bvmState = new BhyveVMState();
		VMStateBase.loadFile(URL, bvmState, this);

		return bvmState;
	}
}
