package org.alvin.code.v2.core.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gzz_gzz@163.com
 * @功能描述:首页的跳转类
 * @date 2018-02-15
 */
@Controller
public class CommonAction {
	/**
	 * @功能描述: 进入主页面的跳转
	 */
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping("/api/heartbeat")
	@ResponseBody
	public Long heartbeat() {
		return System.currentTimeMillis();
	}
}
