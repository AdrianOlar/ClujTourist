package ro.adrian.tourist.ontology;

import android.content.Context;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;

/**
 * Created by Adrian-PC on 4/4/14.
 * Licence thesis project
 */
public class OntologyTest {

    public static void createAndShowOntology(Context context) {
        try {
            //create an empty model
            OntModel jenaModel = createModel();
            //add an empty dataset
            Individual dataset = jenaModel.createIndividual("Dataset URI",
                    jenaModel.getOntClass(OT.OTClass.Dataset.getNS()));
            OT.OTClass.Dataset.createOntClass(jenaModel);
            OT.OTClass.DataEntry.createOntClass(jenaModel);
            OT.OTClass.Feature.createOntClass(jenaModel);
            OT.OTClass.FeatureValue.createOntClass(jenaModel);
            OT.OTClass.Compound.createOntClass(jenaModel);
            //Add an empty Data Entry to the dataset
            Individual dataEntry = jenaModel.createIndividual(OT.OTClass.DataEntry.getOntClass(jenaModel));
            dataset.addProperty(OT.dataEntry, dataEntry);
            //set compound to the data entry
            Individual compound = jenaModel.createIndividual("compoundURI", OT.OTClass.Compound.getOntClass(jenaModel));
            dataEntry.addProperty(OT.compound, compound);
            //add feature values
            //First value
            Individual feature1 = jenaModel.createIndividual("featureURI1", OT.OTClass.Feature.getOntClass(jenaModel));
            Individual featureValue1 = jenaModel.createIndividual(OT.OTClass.FeatureValue.getOntClass(jenaModel));
            featureValue1.addProperty(OT.feature, feature1);
            featureValue1.addLiteral(OT.value, jenaModel.createTypedLiteral("formaldehyde", XSDDatatype.XSDstring));
            //Second value
            Individual feature2 = jenaModel.createIndividual("featureURI2", OT.OTClass.Feature.getOntClass(jenaModel));
            Individual featureValue2 = jenaModel.createIndividual(OT.OTClass.FeatureValue.getOntClass(jenaModel));
            featureValue2.addProperty(OT.feature, feature2);
            featureValue2.addLiteral(OT.value, jenaModel.createTypedLiteral(3.14, XSDDatatype.XSDdouble));

            //and finally add values to the data entry
            dataEntry.addProperty(OT.values, featureValue1);
            dataEntry.addProperty(OT.values, featureValue2);

            //write results
            jenaModel.write(System.out, "RDF/XML-ABBREV");

        } catch (Exception e) {
            System.out.println("EX:" + e);
        }
    }

    public static OntModel createModel() throws Exception {
        OntModel jenaModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, null);
        jenaModel.setNsPrefix("ot", OT.NS);
        jenaModel.setNsPrefix("owl", OWL.NS);
        jenaModel.setNsPrefix("dc", DC.NS);
        return jenaModel;
    }

}
