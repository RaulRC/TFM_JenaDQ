package org.uclm.alarcos.rrc.test.jenadq;

import junit.framework.TestCase;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;
import org.junit.Test;
import org.uclm.alarcos.rrc.dqmodel.DQModel;
import org.uclm.alarcos.rrc.jenadq._dimAccessibility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class JenaDQTest
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
    public void testGetRules(){
        DQModel dqm = new DQModel();
        dqm.setDqmodel(model1);
        BufferedReader brc = new BufferedReader(new InputStreamReader(useRules));
        List<Rule> rules = Rule.parseRules(Rule.rulesParserFromReader(brc));
        assert(rules.size() > 0);
    }
    @Test
    public void test_Accessibility(){
        DQModel dqm = new DQModel();
        dqm.setDqmodel(model1);
        BufferedReader brc = new BufferedReader(new InputStreamReader(useRules));
        BufferedReader crc = new BufferedReader(new InputStreamReader(contextRules));
        List<Rule> useRules = Rule.parseRules(Rule.rulesParserFromReader(brc));
        List<Rule> contextualRules = Rule.parseRules(Rule.rulesParserFromReader(crc));

        _dimAccessibility access = new _dimAccessibility(dqm, useRules, contextualRules, 1, "", "someuri");
        access.setAssessmentIdentifier("testingAccessibility");
        access.setURI("someuri");
        try{
            Model result = access._executeMeasurement();
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
