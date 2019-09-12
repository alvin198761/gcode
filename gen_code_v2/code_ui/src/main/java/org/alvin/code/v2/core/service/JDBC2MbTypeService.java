package org.alvin.code.v2.core.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author 唐植超
 * @date 2019/09/12
 */
@Service
public class JDBC2MbTypeService implements InitializingBean {

    private Properties properties = new Properties();

    public String getType(String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        properties.load(this.getClass().getResourceAsStream("/jdbctypemap.properties"));
    }
}
