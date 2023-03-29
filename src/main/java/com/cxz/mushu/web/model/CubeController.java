package com.cxz.mushu.web.model;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxz.mushu.entity.common.PageParam;
import com.cxz.mushu.entity.common.Result;
import com.cxz.mushu.entity.model.Cube;
import com.cxz.mushu.service.model.CubeService;
import com.cxz.mushu.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/model")
public class CubeController extends BaseController {
	
	@Autowired
	private CubeService service;
	
	@RequestMapping(value="/listCube.action")
	public @ResponseBody
    Object list(){
		return super.buildSucces(service.listCube(null));
	}
	
	@RequestMapping(value="/pageCube.action")
	public @ResponseBody
    Object page(String key, PageParam page){
		PageHelper.startPage(page.getPage(), page.getRows());
		List<Cube> ls = service.listCube(key);
		PageInfo<Cube> pageInfo=new PageInfo<Cube>(ls);
		return super.buildSucces(pageInfo);
	}

	@RequestMapping(value="/saveCube.action", method = RequestMethod.POST)
	public @ResponseBody
    Object save(@RequestBody Cube cube){
		Result ret = service.insertCube(cube);
		return ret;
	}
	
	@RequestMapping(value="/updateCube.action", method = RequestMethod.POST)
	public @ResponseBody
    Object update(@RequestBody Cube cube){
		Result ret = service.updateCube(cube);
		return ret;
	}
	
	@RequestMapping(value="/delCube.action")
	public @ResponseBody
    Object delete(Integer cubeId){
		Result ret = service.deleteCube(cubeId);
		return ret;
	}
	
	@RequestMapping(value="/getCube.action")
	public @ResponseBody
    Object get(Integer cubeId){
		JSONObject cube = service.getCubeById(cubeId);
		return super.buildSucces(cube);
	}
	
	@RequestMapping(value="/treeCube.action")
	public @ResponseBody
    Object tree(Integer cubeId){
		List<Map<String, Object>> ret = service.treeCube(cubeId);
		if(ret.size() == 0){
			return super.buildError("无数据");
		}
		return super.buildSucces(ret);
	}
}
