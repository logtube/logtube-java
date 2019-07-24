package io.github.logtube.apollo;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import io.github.logtube.LogtubeOptionsProvider;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class LogtubeApolloOptionsProvider implements LogtubeOptionsProvider {

    @Override
    public Properties loadOptions() {
        ConfigFile configFile = ConfigService.getConfigFile("logtube", ConfigFileFormat.Properties);
        String content = configFile.getContent();
        if (content == null) {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(content));
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

}
