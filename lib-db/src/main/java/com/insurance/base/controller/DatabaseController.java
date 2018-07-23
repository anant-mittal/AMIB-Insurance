




package com.insurance.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jboss.logging.Param;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.insurance.base.dao.DataDao;
import com.insurance.base.model.Business;
import com.insurance.base.model.Nationality;

import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;

@RestController
public class DatabaseController
{
	@Autowired
	DataDao dao;

	String TAG = "com.insurance.base.controller.DatabaseController :- ";

	@RequestMapping(value = "/viewbusiness", method = RequestMethod.GET, produces = { "application/json" })
	public List<Business> viewBusiness()
	{
		List<Business> businessList = dao.getBusinessData();
		System.out.println("DatabaseController :: ModelAndView :: businessList :" + businessList.size());
		return businessList;
	}

	@RequestMapping(value = "/viewnationality", method = RequestMethod.GET, produces = { "application/json" })
	public List<Nationality> viewNationality()
	{
		List<Nationality> nationalityList = dao.getNationalityData();
		System.out.println("DatabaseController :: ModelAndView :: nationalityList :" + nationalityList.size());
		return nationalityList;
	}

	@RequestMapping(value = "/viewbusinessdemo", method = RequestMethod.POST, produces = { "application/json" })
	public ArrayList viewBusinessDemo()
	{
		try
		{
			return dao.getBusinessDataDemo();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/fileupload", method = RequestMethod.POST)
	public String fileUpload(HttpServletRequest request, @RequestParam MultipartFile fileUpload) throws Exception
	{
		try
		{
			System.out.println("DatabaseController :: fileUpload ");
			dao.fileUpload(fileUpload);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "Ok";
	}

}
