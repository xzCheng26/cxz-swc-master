package com.cxz.mushu.web.portal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.MVContext;
import com.cxz.mushu.entity.portal.BoxQuery;
import com.cxz.mushu.service.portal.BoxService;
import com.cxz.mushu.util.BaseController;
import com.cxz.mushu.util.CompPreviewService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Scope("prototype")
@RequestMapping(value = "/portal")
public class BoxViewController extends BaseController {

	private static Logger logger = Logger.getLogger(BoxViewController.class);
	
	@Autowired
	private BoxService serivce;

	@RequestMapping(value="/BoxView.action", method = RequestMethod.POST)
	public @ResponseBody
    Object tableView(@RequestBody BoxQuery box, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ExtContext.getInstance().removeMV(BoxService.deftMvId);
		try {
			MVContext mv = serivce.json2MV(box);
			CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
			ser.setParams(serivce.getMvParams());
			ser.initPreview();
			String ret = ser.buildMV(mv, req.getServletContext());
			JSONObject json = JSONObject.parseObject(ret);
			if (json.get("result") != null && json.getInteger("result") == 500) {
				return super.buildError(json.getString("msg"));
			}
			json = json.getJSONObject(box.getId());
			return super.buildSucces(json);
		}catch (Exception ex){
			logger.error("数据块展现出错", ex);
			return super.buildError(ex.getMessage());
		}
	}
	
}
