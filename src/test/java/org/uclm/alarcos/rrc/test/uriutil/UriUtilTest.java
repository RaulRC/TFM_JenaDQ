package org.uclm.alarcos.rrc.test.uriutil;

import junit.framework.TestCase;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;
import org.uclm.alarcos.rrc.dqmodel.DQModel;
import org.uclm.alarcos.rrc.utilities.UriUtil;



/**
 * Unit test for simple App.
 */
public class UriUtilTest
    extends TestCase {
    private DQModel dqmodel = new DQModel();


    @Test
    public void testgetURIResourceList(){
        assert(UriUtil.getURIResourceList(ModelFactory.createDefaultModel()).size() == 0);
    }

}
