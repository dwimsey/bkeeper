package us.wimsey.apiary.apiaryd.hypervisors;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class XhyveHypervisorDriver implements IHypervisor {
	final Properties _props;
	public XhyveHypervisorDriver(Properties hypervisorInitializationProperties)
	{
		_props = hypervisorInitializationProperties;
	}

	private String shellCmd = "/bin/bash";
	private String sshShellCmd = "ssh ${SSH_OPTIONS} ${REMOTE} ${SHELLCMD}";

    public String getHypervisorName()
    {
        String hostname = "*Unknown*";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostname;
    }

	public List<String> GetVirtualMachines()
	{
		return null;
	}
}
