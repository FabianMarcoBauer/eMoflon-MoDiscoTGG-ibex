package org.emoflon.ibex.tgg.run.modiscoibextgg;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.emoflon.ibex.tgg.operational.strategies.gen.MODELGEN;
import org.emoflon.ibex.tgg.operational.strategies.gen.MODELGENStopCriterion;
import org.emoflon.ibex.tgg.runtime.engine.DemoclesEngine;

public class MODELGEN_App extends MODELGEN {

	public MODELGEN_App(String projectName, String workspacePath, boolean flatten, boolean debug) throws IOException {
		super(projectName, workspacePath, flatten, debug);
		registerPatternMatchingEngine(new DemoclesEngine());
	}

	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();

		MODELGEN_App generator = new MODELGEN_App("MoDiscoIbexTGG", "./../", false, false);
		
		MODELGENStopCriterion stop = new MODELGENStopCriterion(generator.getTGG());
		stop.setTimeOutInMS(1000);
		generator.setStopCriterion(stop);
		
		logger.info("Starting MODELGEN");
		long tic = System.currentTimeMillis();
		generator.run();
		long toc = System.currentTimeMillis();
		logger.info("Completed MODELGEN in: " + (toc - tic) + " ms");
		
		generator.saveModels();
		generator.terminate();
	}

	protected void registerUserMetamodels() throws IOException {
		//FIXME load and register source and target metamodels
		
		// Register correspondence metamodel last
		loadAndRegisterMetamodel(projectPath + "/model/" + projectPath + ".ecore");
	}
}