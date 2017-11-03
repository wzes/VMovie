package com.wzes.vmovie.util;

import com.wzes.vmovie.bean.Movie;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
/**
 * Created by xuantang on 11/3/17.
 */

public class XmlUtils {

    public static List<Movie> parser(String data){

        List<Movie> list = new ArrayList<>();
        Document document = null;
        try {
            document = DocumentHelper.parseText(data);
        } catch (DocumentException e) {
            MyLog.i(e.getMessage());
        }
        Element root = document.getRootElement();
        System.out.println(root.getName());
        Iterator<Element> movieNodes = root.elementIterator();
        while(movieNodes.hasNext()){
            Element movieNode = movieNodes.next();
            Iterator<Element> mIterator = movieNode.elementIterator();
            while(mIterator.hasNext()){
                Element movie = mIterator.next();
                if(movie.getName().equals("data")) {
                    JSONObject jsonObject = JSONObject.parseObject(movie.getText());
                    Movie m = new Movie();
                    m.setId(jsonObject.getString("id"));
                    m.setTitle(jsonObject.getString("title"));
                    m.setImage(jsonObject.getString("image"));
                    m.setRating(jsonObject.getString("rating"));
                    list.add(m);
                }

            }
            //System.out.println(e.getName());
            //listNodes(e);
        }
        return list;
    }
}
