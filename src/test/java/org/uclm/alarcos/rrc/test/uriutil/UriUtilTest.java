package org.uclm.alarcos.rrc.test.uriutil;

import junit.framework.TestCase;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.junit.Test;
import org.uclm.alarcos.rrc.dqmodel.DQModel;
import org.uclm.alarcos.rrc.utilities.UriUtil;

import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Unit test for simple App.
 */
public class UriUtilTest
    extends TestCase {
    private DQModel dqmodel = new DQModel();
    private final String rdfSource = "src/test/resources/210673-0-eventos-igualdad-inmigrantes-100.rdf";
    private final InputStream in1 = FileManager.get().open(rdfSource);
    private final Model model1 = ModelFactory.createDefaultModel().read(in1, "");

    @Test
    public void testgetURIResourceListEmpty(){
        assert(UriUtil.getURIResourceList(ModelFactory.createDefaultModel()).size() == 0);
    }
    @Test
    public void testgetURIResourceListExample(){
        int size = UriUtil.getURIResourceList(model1).size();
        assert( size == 192);
    }
}
