package us.wimsey.apiary.apiaryd.hypervisors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;

import java.io.File;
import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class XhyveHypervisorDriver extends GenericHypervisor {
	private static final Logger logger = LogManager.getLogger(XhyveHypervisorDriver.class);

	public XhyveHypervisorDriver(Properties hypervisorInitializationProperties)
	{
		super(hypervisorInitializationProperties);
	}

	@Override
	public IVMState registerVm(String URL) {
		return registerVm(new File(URL));
	}

	@Override
	public IVMState registerVm(File URL) {
		return null;
	}

	@Override
	public IVMState create(String vmTemplateName) {
		return null;
	}
}
