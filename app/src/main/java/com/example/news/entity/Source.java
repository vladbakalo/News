package com.example.news.entity;

import com.example.news.retrofit.model.news.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Влад on 31.12.2017.
 */

public class Source {

    public static List<Source> getDefault(){
        List<Source> list = new ArrayList<>();
        list.add(new Source("abc-news"));
        list.add(new Source("abc-news-au"));
        list.add(new Source("aftenposten"));
        list.add(new Source("ansa"));
        list.add(new Source("argaam"));
        return list;
    }

    private String id;

    public Source() {

    }

    public Source(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public static String putSourceNames(List<Source> sources){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < sources.size(); i++) {
            if (i != 0)
                stringBuilder.append(",");
            stringBuilder.append(sources.get(i).getId());
        }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Source{" +
                "id='" + id + '\'' +
                '}';
    }
}
