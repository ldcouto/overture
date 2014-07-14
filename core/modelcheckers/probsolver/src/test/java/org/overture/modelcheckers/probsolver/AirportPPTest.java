package org.overture.modelcheckers.probsolver;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.lex.Dialect;
import org.overture.config.Release;
import org.overture.config.Settings;
import org.overture.modelcheckers.probsolver.AbstractProbSolverUtil.SolverException;
import org.overture.test.framework.ConditionalIgnoreMethodRule.ConditionalIgnore;
import org.overture.typechecker.assistant.TypeCheckerAssistantFactory;

import de.be4.classicalb.core.parser.exceptions.BException;

public class AirportPPTest extends ProbConverterTestBase {

	public AirportPPTest() {
		super(new File("src/test/resources/classes/Airport.vdmpp".replace('/',
				File.separatorChar)), new TypeCheckerAssistantFactory());
	}

	@Before
	public void setup() throws BException {
		super.setup();
		Settings.dialect = Dialect.VDM_PP;
		Settings.release = Release.VDM_10;
	}

	@Test
	@ConditionalIgnore(condition = ProbNotInstalledCondition.class)
	public void testInit() throws IOException, AnalysisException,
			SolverException {
		testMethod("Init");
	}

	@Test
	@ConditionalIgnore(condition = ProbNotInstalledCondition.class)
	public void testGivePermission() throws IOException, AnalysisException,
			SolverException {
		testMethod("GivePermission");
	}

	@Test
	@ConditionalIgnore(condition = ProbNotInstalledCondition.class)
	public void testRecordLanding() throws IOException, AnalysisException,
			SolverException {
		testMethod("RecordLanding");
	}

	@Test
	@ConditionalIgnore(condition = ProbNotInstalledCondition.class)
	public void testRecordTakeOff() throws IOException, AnalysisException,
			SolverException {
		testMethod("RecordTakeOff");
	}

	@Test
	@ConditionalIgnore(condition = ProbNotInstalledCondition.class)
	public void testNumberWaiting() throws IOException, AnalysisException,
			SolverException {
		testMethod("NumberWaiting");
	}

	@Override
	protected String getPropertyId() {
		return "pp.airport";
	}

}
