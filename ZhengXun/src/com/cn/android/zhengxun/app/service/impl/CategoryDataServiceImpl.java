package com.cn.android.zhengxun.app.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.cn.android.zhengxun.app.data.CategoryData;
import com.cn.android.zhengxun.app.service.CategoryDataService;

public class CategoryDataServiceImpl implements CategoryDataService{

	private List<CategoryData> categoryList;
	private Context context;
	
	public CategoryDataServiceImpl(Context context)
	{
		this.context=context;
	}
	@Override
	public List<CategoryData> getData(String path) {
		// TODO Auto-generated method stub
		try {
			parserXML(context.getAssets().open(path));
		} catch (IOException e) {
			Log.e("IOException", "exception", e);
		} catch (XmlPullParserException e) {
			Log.e("XmlPullParserException", "exception", e);
		}
		return categoryList;
	}
	
	private void parserXML(InputStream is) throws XmlPullParserException, IOException {
		
		XmlPullParser parser=Xml.newPullParser();
        parser.setInput(is, "UTF-8");		
		int event=parser.getEventType();
		CategoryData category=null;
		while(event!=XmlPullParser.END_DOCUMENT){
			
			switch(event){
			case XmlPullParser.START_DOCUMENT:
				categoryList=new ArrayList<CategoryData>();
				break;				
			case XmlPullParser.START_TAG:
				if("item".equals(parser.getName()))
				{
					category=new CategoryData();
				}else if("id".equals(parser.getName())){
					category.setId(Integer.parseInt(parser.nextText().trim()));
				}else if("typeId".equals(parser.getName())){
					category.setTypeId(Integer.parseInt(parser.nextText().trim()));
				}else if("name".equals(parser.getName()))
				{
					category.setName(parser.nextText());
				}else if("pic".equals(parser.getName()))
				{
					category.setPicPath(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if("item".equals(parser.getName()))
				{
					categoryList.add(category);
					category=null;
				}
				break;
			}
			event=parser.next();
		}
		if(null!=is)
		{
			is.close();
		}
		
	}
	public List<CategoryData> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<CategoryData> categoryList) {
		this.categoryList = categoryList;
	}

	
}
