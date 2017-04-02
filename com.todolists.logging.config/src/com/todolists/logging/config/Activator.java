package com.todolists.logging.config;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * http://www.vogella.com/tutorials/EclipseLogging/article.html#slf4j-logback-
 * from-orbit
 *
 * Usually Logback is configured by a logback.xml or logback-test.xml file.
 *
 * In an OSGi environment it is not that easy for Logback to automatically
 * determine where the logback.xml logger configuration is stored.
 *
 * For Logback the ch.qos.logback.classic.joran.JoranConfigurator is used to
 * parse the logback.xml configuration file.
 *
 * By obtaining the ch.qos.logback.classic.LoggerContext and using the
 * JoranConfigurator the location of the logback.xml file can be set
 * programmatically.
 *
 * Create a plug-in project with an Activator. The root folder of this plug-in
 * should contain the logback.xml file.
 *
 * The following dependencies are required to configure Logback : see
 * dependencies
 *
 * The configuration of the SLF4J implementation (Logback) should be seperated
 * from the other bundles. So that no dependency to the
 * com.vogella.logging.config bundle is necessary and other bundles just need to
 * import the org.slf4j package, like it is done com.vogella.logging.rcp.
 *
 * Due to that the com.vogella.logging.config bundle will not be started
 * automatically. Therefore it must be added to the Start Levels in the product
 * configuration.
 *
 * Add the plugin in feature and open the product file. Select the
 * Configuration-Tab and add the logging-Plugin in Start Levels. After this, set
 * the start level to 0 and Auto-Start to true.
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return Activator.context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		this.configureLogbackInBundle(bundleContext.getBundle());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
	}

	private void configureLogbackInBundle(final Bundle bundle) throws JoranException, IOException {
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		final JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(context);
		context.reset();

		// this assumes that the logback.xml file is in the root of the bundle.
		final URL logbackConfigFileUrl = FileLocator.find(bundle, new Path("logback.xml"), null);
		jc.doConfigure(logbackConfigFileUrl.openStream());
	}
}
