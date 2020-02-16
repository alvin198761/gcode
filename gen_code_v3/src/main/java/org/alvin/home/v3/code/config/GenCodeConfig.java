package org.alvin.home.v3.code.config;

import lombok.extern.slf4j.Slf4j;
import org.alvin.home.v3.code.system.gencode.GenCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootConfiguration
public class GenCodeConfig {

    @Autowired
    private GenCodeService genCodeService;

    @PostConstruct
    public void initConfigTable() {
        log.info("执行初始化sql");
        this.genCodeService.initData("/sql/create_config_table.sql");
        this.genCodeService.initData("/sql/create_ref_table.sql");
    }
}
