package us.wimsey.apiary.apiaryd.hypervisors;

import java.util.List;

/**
 * Created by dwimsey on 7/9/16.
 */
public interface IHypervisor {
    public String getHypervisorName();
	public List<String> getVMList();

	public void shutdown(float gracePeriod);
}
