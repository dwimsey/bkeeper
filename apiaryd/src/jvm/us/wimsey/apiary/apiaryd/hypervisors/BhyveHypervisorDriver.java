package us.wimsey.apiary.apiaryd.hypervisors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class BhyveHypervisorDriver extends GenericHypervisor {
	private static final Logger logger = LogManager.getLogger(BhyveHypervisorDriver.class);

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
}
