/**
 * Copyright 2016 David Wimsey - All rights reserved.
 * Author: David Wimsey <david@wimsey.us>
 */

package us.wimsey.apiary.apiaryd;

import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.cli.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import us.wimsey.apiary.apiaryd.virtualmachines.IVMState;
import us.wimsey.apiary.utils.PropertyResources;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Properties;

public class DaemonInstance {
	private static final Logger LOG = Logger.getLogger(DaemonInstance.class);

	private static Properties props = null;
	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure(); // Basic console output for log4j
		LogManager.getRootLogger().setLevel(Level.INFO);

		Options options = new Options();
		options.addOption("help", false, "Display help");
		options.addOption("defaults", false, "Show defaults file");
		options.addOption("nodefaults", false, "Ignore defaults file");
		options.addOption("properties", true, "Properties file to load for configuration properties");

		CommandLineParser parser = new DefaultParser();
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
		} catch(UnrecognizedOptionException uoe) {
			System.err.println("Error with options specified: " + uoe.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "apiaryd", options );
			return;
		}
		if(line.hasOption("help") == true) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "apiaryd", options );
			return;
		}

		String propertiesFile = "apiaryd.properties";

		props = new Properties();
		if(line.hasOption("defaults") == true) {
			Properties p = PropertyResources.loadPropertiesResourceFile(new Properties(), Paths.get("us", "wimsey", "apiary", "apiaryd", propertiesFile).toString());
			StringWriter writer = new StringWriter();
			p.list(new PrintWriter(writer));
			System.out.println("apiaryd defaults:");
			System.out.println(writer.getBuffer().toString());
			return;
		} else if(line.hasOption( "nodefaults" ) == false) {
			props = PropertyResources.loadPropertiesResourceFile(props, Paths.get("us", "wimsey", "apiary", "apiaryd", propertiesFile).toString());
			// try to load the file from the users home directory as a .dotfile
			String home = System.getProperty("user.home");
			File propsFile = new File(home, "." + propertiesFile);
			props = PropertyResources.loadPropertiesFile(props, propsFile.getAbsolutePath());
		}

		if( line.hasOption( "properties" ) == true) {
			if (line.getOptionValue("properties") != null) {
				propertiesFile = line.getOptionValue("properties");
				props = PropertyResources.loadPropertiesFile(props, propertiesFile);
			}
		}

		short listenerPort = 8888;
		if(props.containsKey("apiaryd.http.port")) {
			listenerPort = Short.parseShort(props.getProperty("apiaryd.http.port"));

		}

		VMMonitor vmMonitor = new VMMonitor(props);
		APIServer apiServer = new APIServer(listenerPort, props, vmMonitor);

		vmMonitor.start();
		try {
			apiServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		} catch (IOException ioe) {
			System.err.println("Couldn't start server:\n" + ioe);
			System.exit(-1);
		}

		System.out.println("Server started, Hit Enter to stop.  Listening on port " + apiServer.getListeningPort() + ".\n");

		IVMState vmf = null;
		boolean foundVm = false;
		int is = vmMonitor.virtualMachines.size();
		for (int i = 0; i < is; i++) {
			vmf = vmMonitor.virtualMachines.get(i);
			if("vdc-02.wimsey.us".equals(vmf.getVMName()) == true) {
				break;
			} else {
				vmf = null;
			}
		}

		if(vmf == null) {
			//vmf = vmMonitor.register("/mnt/vm-nfs-01/bhyve/vdc-02.wimsey.us/vdc-02.wimsey.us.apiary");
			vmf = vmMonitor.create("windows");
			vmf.save("/mnt/vm-nfs-01/bhyve/vdc-02.wimsey.us/vdc-02.wimsey.us.apiary")
		}
		//if(vmf.isRunning() == false) {
		//	vmf.start();
		//}
		// This launches our windows VDC, hard coded for now cause we got too many missing parts
		/*
		     -s 0,hostbridge \
      -s 3,ahci-hd,/dev/zvol/zfs01/vm-nfs-01/bhyve/vdc-02.wimsey.us/disk01  \
      -s 10,virtio-net,tap0 \
      -s 31,lpc \
      -l com1,/dev/nmdm0A \
      -l com2,/dev/nmdm1A \
      -l bootrom,/apiary/system/efi/BHYVE_UEFI_20151002.fd \
      -m 4G -H -w \
      vdc-02.wimsey.us


		 */
		try {
			System.in.read();
		} catch (Throwable ignored) {
		}

		apiServer.stop();
		System.out.println("API Server stopped.\n");

		// We'll shut down the vm's depending on a sigterm or sighup
		vmMonitor.shutdown(false);
	}
}
