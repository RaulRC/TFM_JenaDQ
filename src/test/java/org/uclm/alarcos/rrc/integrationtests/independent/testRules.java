package org.uclm.alarcos.rrc.integrationtests.independent;


import junit.framework.TestCase;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.reasoner.rulesys.Rule;
import org.junit.Test;
import org.uclm.alarcos.rrc.jenadq.*;
import org.uclm.alarcos.rrc.jenadqexceptions.AssessmentException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import static org.apache.jena.vocabulary.OWLTest.Test;

public class testRules extends TestCase {

	@org.junit.Test
	public void testRules() throws FileNotFoundException, AssessmentException {
		try{
			// RULES
			FileReader in = new FileReader("src/test/resources/use.rules");
			BufferedReader br = new BufferedReader(in);
			String endpoint = "http://dbpedia.org/sparql";
			// String endpoint = "http://lod.openlinksw.com/sparql";
			String uri = "http://dbpedia.org/resource/Manakkara";
			String uri2 = "http://dbpedia.org/resource/La_Solana";
			String uri3 = "http://dbpedia.org/resource/Life_of_Pi";
			int depth = 0;
			List rules = Rule.parseRules(Rule.rulesParserFromReader(br));
			FileReader in2 = new FileReader("src/test/resources/contextual.rules");
			BufferedReader br2 = new BufferedReader(in2);
			List contextualRules = Rule.parseRules(Rule.rulesParserFromReader(br2));

			System.out.println(rules.toString());
			System.out.println(contextualRules.toString());

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

			dqplan.addDQAssessment(new DQAssessment(dqdimlist, uri, endpoint,
					contextualRules, rules, 0, "FIRST_ASSESSMENT"));
			dqplan.addDQAssessment(new DQAssessment(dqdimlist2, uri2, endpoint,
					contextualRules, rules, 0, "SECOND_ASSESSMENT"));
			dqplan.addDQAssessment(new DQAssessment(dqdimlist, uri3, endpoint,
					contextualRules, rules, 0, "THIRD_ASSESSMENT"));

// --- COMPARISON MODEL
			// FIRST MODEL
//		dqplan.executePlan();
//		Model mod = dqplan.getFinalModel();
//		System.out.println("First model completed");

//		dqplan.getAssessmentList().clear();
//		dqplan.addDQAssessment(new DQAssessment(dqdimlist2, uri2, endpoint,
//				contextualRules, rules, 0, "IDENTIFIER"));

			// SECOND MODEL
//		dqplan.executePlan();
//		Model mod2 = dqplan.getFinalModel();
//		System.out.println("Second model completed");
//
//		DQModel dqm = new DQModel(mod2, "N3");
//		Model comparison = dqm.compareModelWith(mod);
//
//		comparison.write(System.out, "N3");
//		mod.write(System.out, "TTL");
// --------- COMPARISON END

			dqplan.executePlan();
			Model mod = dqplan.getFinalModel();
			mod.write(System.out, "TTL");
			assert(mod.size()>0);
		}
		catch(Exception e){
			e.printStackTrace();
			assert(false);
		}

	}

}
