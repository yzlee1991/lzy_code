
package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * author :lzy
 * date   :2017��9��8������3:32:19
 */

@RequestMapping("/auth")
@Controller
public class Auth {

	@RequestMapping()
	public String auth(){
		return "auth";
	}
	
}
