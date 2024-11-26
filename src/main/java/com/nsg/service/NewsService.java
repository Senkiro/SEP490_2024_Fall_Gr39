package com.nsg.service;

import com.nsg.dto.request.news.NewsRequest;
import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.entity.NewEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NewsService {
    //create new news
    NewsResponse createNews(NewsRequest request);

    Page<NewsResponse> getAllNews(int page, int size);

    Page<NewsResponse> getAllPublishNews(int page, int size);

    //update news
    NewsResponse updateNews(String newsId, NewsRequest request);

    NewsResponse getNews(String newsId);

    //delete room by id
    void deleteNews(String newsId);

    NewEntity getNewtById(String newsId);
}
