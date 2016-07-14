package us.wimsey.apiary.apiaryd.hypervisors.bhyve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.hypervisors.GenericHypervisor;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;
import us.wimsey.apiary.apiaryd.virtualmachines.VMStateBase;
import us.wimsey.apiary.apiaryd.virtualmachines.devices.factories.*;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class BhyveHypervisorDriver extends GenericHypervisor {
    private static final Logger logger = LogManager.getLogger(BhyveHypervisorDriver.class);

    private String sshShellCmd = "ssh ${SSH_OPTIONS} ${REMOTE} ${SHELLCMD}";

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

        deviceFactories.put("hvmoptions", new CmdLineOptionsDeviceFactory(this));
        deviceFactories.put("hostbridge", new PCIHostbridgeFactory(this));
        deviceFactories.put("lpc", new LPCBridgeFactory(this));
        deviceFactories.put("ahci-hd", new BlockDeviceFactory(this));
        deviceFactories.put("ahci-cd", new BlockDeviceFactory(this));
        deviceFactories.put("virtio-blk", new BlockDeviceFactory(this));
        deviceFactories.put("virtio-net", new NICDeviceFactory(this));
        deviceFactories.put("virtio-rnd", new RNDDeviceFactory(this));
    }

    @Override
    public IVMState registerVm(File URL) {
        BhyveVMState vmState = new BhyveVMState();
        vmState.loadFile(URL, this);
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
