package org.emoflon.ibex.tgg.run.modiscoibextgg;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmt.modisco.java.emf.JavaPackage;
import org.eclipse.gmt.modisco.java.emf.impl.JavaPackageImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.impl.UMLPackageImpl;
import org.emoflon.ibex.tgg.operational.strategies.sync.SYNC;
import org.emoflon.ibex.tgg.runtime.engine.DemoclesEngine;

@SuppressWarnings("restriction")
public class SYNC_App extends SYNC {

	public SYNC_App(String projectName, String workspacePath, boolean flatten, boolean debug) throws IOException {
		super(projectName, workspacePath, flatten, debug);
		registerPatternMatchingEngine(new DemoclesEngine());
	}

	public static void main(String[] args) throws IOException {
		boolean forward = true;
		boolean backward = true;

		if (args.length > 0) {
			if (args[0].matches("b(ackward(s)?)?")) {
				backward = false;
			} else if (args[0].matches("f(orward(s)?)?")) {
				forward = false;
			}
		}

		BasicConfigurator.configure();

		if (forward) {
			SYNC_App sync = new SYNC_App("MoDiscoIbexTGG", "./../", false, false);
			logger.info("Starting SYNC");
			long tic = System.currentTimeMillis();
			sync.forward();
			long toc = System.currentTimeMillis();
			logger.info("Completed SYNC in: " + (toc - tic) + " ms");
			sync.saveModels();
			sync.terminate();
		}

		if (backward) {
			SYNC_App sync = new SYNC_App("MoDiscoIbexTGG", "./../", false, false);
			logger.info("Starting SYNC");
			long tic = System.currentTimeMillis();
			sync.backward();
			long toc = System.currentTimeMillis();
			logger.info("Completed SYNC in: " + (toc - tic) + " ms");
			sync.saveModels();
			sync.terminate();
		}

	}

	@Override
	public void loadModels() throws IOException {
		s = createResource(projectPath + "/instances/tmp.xmi");
		t = createResource(projectPath + "/instances/tmp.xmi");
		c = createResource(projectPath + "/instances/tmp.xmi");
		p = createResource(projectPath + "/instances/tmp.xmi");

		EcoreUtil.resolveAll(rs);
	}

	@Override
	public void forward() throws IOException {
		s = loadResource(projectPath + "/instances/src_processed.xmi");
		t = createResource(projectPath + "/instances/fwd.trg.xmi");
		c = createResource(projectPath + "/instances/fwd.corr.xmi");
		p = createResource(projectPath + "/instances/fwd.protocol.xmi");

		super.forward();
	}

	@Override
	public void backward() throws IOException {
		s = createResource(projectPath + "/instances/bwd.trg.xmi");
		t = loadResource(projectPath + "/instances/trg_processed.xmi");
		c = createResource(projectPath + "/instances/bwd.corr.xmi");
		p = createResource(projectPath + "/instances/bwd.protocol.xmi");

		super.backward();
	}

	protected void registerUserMetamodels() throws IOException {
		rs.getPackageRegistry().put("platform:/plugin/org.eclipse.gmt.modisco.java/model/java.ecore",
				JavaPackageImpl.init());
		rs.getPackageRegistry().put("platform:/plugin/org.eclipse.uml2.uml/model/UML.ecore", UMLPackageImpl.init());
		rs.getPackageRegistry().put(JavaPackage.eNS_URI, JavaPackage.eINSTANCE);
		rs.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

		// Register correspondence metamodel last
		loadAndRegisterMetamodel(projectPath + "/model/" + projectPath + ".ecore");
	}
}
