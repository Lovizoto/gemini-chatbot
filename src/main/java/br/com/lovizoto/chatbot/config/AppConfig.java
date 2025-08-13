package br.com.lovizoto.chatbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Não foi possível encontrar o arquivo application.properties");
                throw new IOException("Arquivo de configuração não encontrado");
            }
            properties.load(input);
            logger.info("Configurações carregadas com sucesso");
        } catch (IOException e) {
            logger.error("Erro ao carregar o arquivo de configuração.", e);

        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    //previne instanciação
    private AppConfig() {}

}
