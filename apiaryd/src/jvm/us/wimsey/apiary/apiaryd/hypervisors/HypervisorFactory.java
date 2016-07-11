package us.wimsey.apiary.apiaryd.hypervisors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.wimsey.apiary.apiaryd.hypervisors.bhyve.BhyveHypervisorDriver;
import us.wimsey.apiary.apiaryd.hypervisors.xhyve.XhyveHypervisorDriver;

import java.util.Properties;

/**
 * Created by dwimsey on 7/9/16.
 */
public class HypervisorFactory {
	private static final Logger logger = LogManager.getLogger(HypervisorFactory.class);

	public enum BuiltinHypervisorTypes {
        bhyve ((short)1),
        xhyve ((short)2);

        private final short hypervisorTypeId;
        BuiltinHypervisorTypes(short _hypervisorTypeId)
        {
            this.hypervisorTypeId = _hypervisorTypeId;
        }
    }

    static IHypervisor localHypervisor = null;

    public static IHypervisor getLocalHypervisor(Properties apiaryProperties) throws Exception {
        if(localHypervisor == null) {
			String localHypervisorType = "auto";
			if(apiaryProperties.containsKey("apiaryd.hypervisor.type") == true) {
				localHypervisorType = apiaryProperties.getProperty("apiaryd.hypervisor.type");
			} else {
				String osName = System.getProperty("os.name");

				switch (localHypervisorType) {
					case "Mac OS X":
						localHypervisorType = "xhyve";
						break;
					case "FreeBSD":
						localHypervisorType = "bhyve";
						break;
					default:
						System.err.println(osName);
						localHypervisorType = "*unknown*";
						break;
				}
			}

			switch (localHypervisorType) {
				case "xhyve":
					localHypervisor = new XhyveHypervisorDriver(apiaryProperties);
					break;
				case "bhyve":
					localHypervisor = new BhyveHypervisorDriver(apiaryProperties);
					break;
				default:
					System.err.println(localHypervisorType);
					throw new Exception("Invalid hypervisor type: " + localHypervisorType);
			}


			// Override the default hypervisor detectiong logic



		}
        return localHypervisor;
    }
}
