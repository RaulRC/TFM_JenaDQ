package org.uclm.alarcos.rrc.test.jenadq;

import junit.framework.TestCase;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.junit.Test;
import org.uclm.alarcos.rrc.dqmodel.DQModel;

import java.io.InputStream;

/**
 * Unit test for simple App.
 */
public class JenaDQTest
    extends TestCase{
    private DQModel dqmodel = new DQModel();
    private final String rdfSource = "src/test/resources/210673-0-eventos-igualdad-inmigrantes-100.rdf";
    private final InputStream in1 = FileManager.get().open(rdfSource);
    private final Model model1 = ModelFactory.createDefaultModel().read(in1, "");

/*    @Test
    public void testCreateModel(){
        DQModel dqm = new DQModel(in1, "rdf");
        assert(dqm.getModel().size() > 0);
    }*/


}
