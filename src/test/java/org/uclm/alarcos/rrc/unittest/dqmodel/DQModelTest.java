package org.uclm.alarcos.rrc.unittest.dqmodel;

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
public class DQModelTest
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

    @Test
    public void testDQModel(){
        assert(true);
    }

    @Test
    public void testcompareModelWithSelf(){
        dqmodel.setDqmodel(model1);
        assert(dqmodel.compareModelWith(model1).size()==0);
    }
    @Test
    public void testcompareModelWithNone(){
        dqmodel.setDqmodel(model1);
        assert(dqmodel.compareModelWith(ModelFactory.createDefaultModel()).size()==388);
    }
    @Test
    public void testAffinity(){
        dqmodel.setDqmodel(model1);

        assert(dqmodel.affinity(dqmodel)==100.0);
    }

}
