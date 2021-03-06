package com.baijiayi.app.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.baijiayi.app.AppException;

/**
 * 评论列表实体类
 * @author liux 
 * @version 1.0
 * @created 2012-3-21
 */
public class GoodsCommentList extends Entity{
	
	public final static int CATALOG_GOODS = 0;
	
	private int pageSize;
	private int allCount;
	private List<CommentModel> commentlist = new ArrayList<CommentModel>();
	
	public int getPageSize() {
		return pageSize;
	}
	public int getAllCount() {
		return allCount;
	}
	public List<CommentModel> getCommentlist() {
		return commentlist;
	}

	public static GoodsCommentList parse(String commentsResult) throws IOException, AppException {
		GoodsCommentList commlist = new GoodsCommentList();
        try { 
        	 JSONObject goodsJSON=new JSONObject(commentsResult);
             commlist.allCount = goodsJSON.optInt("count", 0);
             if(commlist.allCount == 0)
             {
             	return commlist;
             }
             
             org.json.JSONArray  itemsArray = goodsJSON.optJSONArray("data");
             if(itemsArray!=null)
             {
            	 commlist.pageSize = itemsArray.length();
             	for(int i=0,c=itemsArray.length();i<c;i++)
             	{
             		JSONObject item = (JSONObject)itemsArray.get(i);
             		CommentModel comment = CommentModel.parse(item);
             		if(comment!=null)
             		{
             			commlist.getCommentlist().add(comment);
             		}
             	}
             }
        } catch (Exception e) {
			throw AppException.xml(e);
        }    
        return commlist;       
	}     
}
