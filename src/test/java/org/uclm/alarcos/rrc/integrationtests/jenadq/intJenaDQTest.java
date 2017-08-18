package org.uclm.alarcos.rrc.integrationtests.jenadq;

import junit.framework.TestCase;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;
import org.junit.Test;
import org.uclm.alarcos.rrc.dqmodel.DQModel;
import org.uclm.alarcos.rrc.jenadq._dimAccessibility;
import org.uclm.alarcos.rrc.jenadq._dimCompleteness;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class intJenaDQTest
    extends TestCase{
    private DQModel dqmodel = new DQModel();
    private final String rdfSource = "src/test/resources/210673-0-eventos-igualdad-inmigrantes-100.rdf";
    private final String useRuleSource = "src/test/resources/use.rules";
    private final String contextualRuleSource = "src/test/resources/contextual.rules";
    private final InputStream in1 = FileManager.get().open(rdfSource);
    private final InputStream useRules = FileManager.get().open(useRuleSource);
    private final InputStream contextRules = FileManager.get().open(contextualRuleSource);
    private final Model model1 = ModelFactory.createDefaultModel().read(in1, "");

    @Test
    public void test_Completeness(){
        DQModel dqm = new DQModel();
        dqm.setDqmodel(model1);
        BufferedReader brc = new BufferedReader(new InputStreamReader(useRules));
        BufferedReader crc = new BufferedReader(new InputStreamReader(contextRules));
        List<Rule> useRules = Rule.parseRules(Rule.rulesParserFromReader(brc));
        List<Rule> contextualRules = Rule.parseRules(Rule.rulesParserFromReader(crc));

        _dimCompleteness completeness = new _dimCompleteness(dqm, useRules, contextualRules, 0,
                "http://dbpedia.org/sparql", "http://es.dbpedia.org/resource/Metallica");
        completeness.setAssessmentIdentifier("testingCompleteness");

        completeness.setURI("someuri");
        completeness.setContextualRules(contextualRules);
        try{
            Model result = completeness._executeMeasurement();
            result.write(System.out);
            DQModel dqmResult = new DQModel();
            dqmResult.setDqmodel(result);
            assert(result.size() > 0);
        }
        catch (Exception e){
            e.printStackTrace();
            assert(false);
        }
    }


}
