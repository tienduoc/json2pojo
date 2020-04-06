package cooltrickshome;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.Rule;
import org.jsonschema2pojo.rules.RuleFactory;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public class JsonToPojo {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String packageName = "com.cooltrickshome";
		File inputJson = new File("." + File.separator + "input.json");
		File outputPojoDirectory = new File("." + File.separator + "convertedPojo");
		outputPojoDirectory.mkdirs();
		try {
			new JsonToPojo().convert2JSON(inputJson.toURI().toURL(), outputPojoDirectory, packageName,
					inputJson.getName().replace(".json", ""));
		} catch (IOException e) {
			System.out.println("Encountered issue while converting to pojo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void convert2JSON(URL inputJson, File outputPojoDirectory, String packageName, String className)
			throws IOException {
		GenerationConfig config = new DefaultGenerationConfig() {
			@Override
			public boolean isGenerateBuilders() { // set config option by overriding method
				return true;
			}

			public SourceType getSourceType() {
				return SourceType.JSON;
			}
		};
		RuleFactory ruleFactory = new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore());
		SchemaMapper mapper = new SchemaMapper(ruleFactory, new SchemaGenerator());
		JCodeModel codeModel = new JCodeModel();
		mapper.generate(codeModel, className, packageName, inputJson);
		codeModel.build(outputPojoDirectory);
	}
}