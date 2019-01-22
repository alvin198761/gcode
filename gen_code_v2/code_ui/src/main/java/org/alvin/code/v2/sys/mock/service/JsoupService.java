package org.alvin.code.v2.sys.mock.service;

import org.alvin.code.v2.sys.mock.bean.JSMock;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JsoupService {

    /**
     * 获取所有接口
     *
     * @param url
     * @return
     * @throws IOException
     */
    public List<JSMock> getMockList(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element element = doc.getElementById("resources");
        Elements actionEls = element.getAllElements();
        return actionEls.stream().map(item -> {
            JSMock jsMock = new JSMock();
            jsMock.setId(item.id());
            Element head = item.getElementsByTag("h2").get(0);
            jsMock.setDescription(head.text());
            jsMock.setName(head.getElementsByTag("a").get(0).text());
            return jsMock;
        }).collect(Collectors.toList());
    }
}
