package org.alvin.code.v2.es;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.alvin.code.gen.beans.RestfullResp;
import org.alvin.code.v2.core.model.CodeCond;
import org.alvin.code.v2.core.model.Field;
import org.alvin.code.v2.core.model.Table;
import org.alvin.code.v2.core.service.VmFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 做一套es 代码生成逻辑
 */
@Service
public class EsClientService {
    ///_cat/indices?v
    @Value("${es.server.url}")
    private String url;
    @Value("${es.server.auth_user}")
    private String user;
    @Value("${es.server.auth_password}")
    private String password;
    @Value("${es.server.index_name}")
    private String indexName;
    @Autowired
    public RestTemplate restTemplate;
    @Autowired
    private VmFileService vmFileService;

    private String getUrl(String reqUrl, String params) {
        reqUrl = url.concat(reqUrl);
        String paramUrl = "";
        if (!Strings.isNullOrEmpty(user)) {
            paramUrl = paramUrl.concat("?auth_user=" + user);
            if (!Strings.isNullOrEmpty(password)) {
                paramUrl = paramUrl.concat("?auth_password=" + password);
            }
        }
        if (paramUrl.trim().isEmpty()) {
            paramUrl = paramUrl.concat("?");
        } else {
            paramUrl = paramUrl.concat("&");
        }
        return reqUrl.concat(paramUrl).concat(params);
    }


    public List<String> indices() {
        String lines = restTemplate.getForObject(getUrl("/_cat/indices", "v&index=" + indexName), String.class);
        return null;
    }

    public RestfullResp<Map<String, Object>> create(CodeCond cond) throws Exception {
        String url = getUrl("/" + indexName + "/_mapping", "pretty=true");
        String data = restTemplate.getForObject(url, String.class);
        JSONObject indexMap = JSONObject.parseObject(data);
        JSONObject index = indexMap.getJSONObject(indexName);
        JSONObject mappings = index.getJSONObject("mappings");
        List<Table> tables = mappings.keySet().stream().map(k -> {
            Table t = new Table();
            t.setC_name(k);
            t.setT_name(k);
            t.setComment(k);
            t.setCls_upp(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, k));
            JSONObject properties = mappings.getJSONObject("properties");
            List<Field> fields = parseFields(properties);
            return t;
        }).collect(Collectors.toList());
        return null;
    }

    /**
     * 解析属性
     *
     * @param properties
     * @return
     */
    private List<Field> parseFields(JSONObject properties) {
        List<Field> fields = Lists.newArrayList();
        properties.keySet().stream().map(k -> {
            JSONObject p = properties.getJSONObject(k);
            String type = p.getString("type");
            return null;
        });
        return fields;
    }

}
