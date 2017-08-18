package org.uclm.alarcos.rrc.integrationtests.independent;


import junit.framework.TestCase;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.reasoner.rulesys.Rule;
import org.junit.Test;
import org.uclm.alarcos.rrc.jenadq.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.LinkedList;
import java.util.List;

public class testPaper extends TestCase {

	@Test
	public void testEnd2End() throws FileNotFoundException {
		try{
			// RULES
			FileReader in = new FileReader("src/test/resources/use.rules");
			BufferedReader br = new BufferedReader(in);
			String endpoint = "http://dbpedia.org/sparql";
//		 String endpoint = "http://lod.openlinksw.com/sparql";
			String uri = "http://dbpedia.org/resource/Manakkara";
			String uri2 = "http://dbpedia.org/resource/Metallica";
			String uri3 = "http://dbpedia.org/resource/Life_of_Pi";
			int depth = 0;
			List rules = Rule.parseRules(Rule.rulesParserFromReader(br));
			FileReader in2 = new FileReader("src/test/resources/contextual.rules");
			BufferedReader br2 = new BufferedReader(in2);
			List contextualRules = Rule.parseRules(Rule.rulesParserFromReader(br2));

			System.out.println(rules.toString());
			System.out.println(contextualRules.toString());

			// TODO - API
			// SETTING PLAN - DQAPLAN
			DQAssessmentPlan dqplan = new DQAssessmentPlan();
			LinkedList<DQAssessment> dqplanlist = new LinkedList<DQAssessment>();
			dqplan.setAssessmentList(dqplanlist);

			// SETTING LIST OF DIMENSIONS I'm GOING TO ASSESS
			LinkedList<DQDimension> dqdimlist = new LinkedList<DQDimension>();
			dqdimlist.add((DQDimension) new _dimAccessibility());
			dqdimlist.add((DQDimension) new _dimCompleteness());

			LinkedList<DQDimension> dqdimlist2 = new LinkedList<DQDimension>();
			dqdimlist2.add((DQDimension) new _dimAccessibility());

			// ADDING ASSESSMENTS
//		dqplanlist.add(new DQAssessment(dqdimlist, uri, endpoint,
//				contextualRules, rules, depth, "IDENTIFIER_GENERATED"));
//		dqplanlist.add(new DQAssessment(dqdimlist, uri2, endpoint,
//				contextualRules, rules, 1, "IDENTIFIER_GENERATED"));

			dqplan.addDQAssessment(new DQAssessment(dqdimlist2, uri3, endpoint,
					contextualRules, rules, 1, "IDENTIFIER_GENERATED"));
//		dqplan.addDQAssessment(new DQAssessment(dqdimlist2, uri2, endpoint,
//				contextualRules, rules, 0, "ANOTHER_IDENTIFIER"));
//		dqplan.addDQAssessment(new DQAssessment(dqdimlist, uri3, endpoint,
//				contextualRules, rules, 1, "ANOTHER_ONEMORETIME"));

			// EXECUTE PLAN
			// TIMES
			long startTime = System.currentTimeMillis();
			Model result = dqplan.executePlan();
			long estimatedTime = System.currentTimeMillis() - startTime;
			System.out.println("TIME ELAPSED: "+ (double) (estimatedTime/1000.0));
			assert(result.size() > 0);
		}
		catch(Exception e){
			e.printStackTrace();
			assert(false);
		}


	}

}
