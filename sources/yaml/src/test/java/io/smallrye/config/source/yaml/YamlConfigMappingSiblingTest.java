package io.smallrye.config.source.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import io.smallrye.config.WithName;
import io.smallrye.config.source.yaml.YamlConfigMappingSiblingTest.Parent.GrandChild;

class YamlConfigMappingSiblingTest {
    @Test
    void yamlConfigMapping() throws Exception {
        SmallRyeConfig config = new SmallRyeConfigBuilder()
                .withMapping(Parent.class, "parent")
                .withSources(new YamlConfigSource(YamlConfigSourceTest.class.getResource("/example-mapping-issue.yml")))
                .build();

        Parent parent = config.getConfigMapping(Parent.class);

        GrandChild goodGrandChild = parent.goodChildren().get("child1");
        assertNotNull(goodGrandChild);
        assertEquals("John", goodGrandChild.name());
        assertEquals("somevalue", goodGrandChild.attributes().get("somekey"));
        assertEquals("anothervalue", goodGrandChild.attributes().get("anotherkey"));

        GrandChild badGrandChild = parent.badChildren().get("child3");
        assertNotNull(badGrandChild);
        assertEquals("BadJohn", badGrandChild.name());
        assertEquals("somevaluebad", badGrandChild.attributes().get("somekeybad"));
        assertEquals("anothervaluebad", badGrandChild.attributes().get("anotherkeybad"));

    }

    @ConfigMapping(prefix = "parent")
    public interface Parent {

        @WithName("goodchildren")
        Map<String, GrandChild> goodChildren();

        @WithName("badchildren")
        Map<String, GrandChild> badChildren();

        interface GrandChild {
            @WithName("name")
            String name();
            @WithName("attributes")
            Map<String, String> attributes();
        }
    }




}
