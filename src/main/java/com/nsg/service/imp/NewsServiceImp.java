package com.nsg.service.imp;

import com.nsg.Mapper.NewsMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.news.NewsRequest;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.entity.NewEntity;
import com.nsg.repository.BatchRepository;
import com.nsg.repository.NewsRepository;
import com.nsg.service.NewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class NewsServiceImp implements NewsService {
    @Autowired
    NewsRepository newsRepository;

    @Autowired
    BatchRepository batchRepository;

    @Override
    public NewsResponse createNews(NewsRequest request) {
        NewEntity news = new NewEntity();

        //map
        BeanUtils.copyProperties(request, news);

        return NewsMapper.INSTANE.toNewsResponse(newsRepository.save(news));
    }

    @Override
    public Page<NewsResponse> getAllNews(int page, int size) {
        Page<NewEntity> newEntitiesList = newsRepository.findAll(PageRequest.of(page, size));

        List<NewsResponse> responseList = newEntitiesList.stream()
                .map(newEntity -> new NewsResponse(
                        newEntity.getNewId(),
                        newEntity.getNewContent(),
                        newEntity.getNewTitle(),
                        newEntity.isStatus(),
                        newEntity.getCreatedDate(),
                        newEntity.getCreatedBy()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, newEntitiesList.getPageable(), newEntitiesList.getTotalElements());
    }

    @Override
    public Page<NewsResponse> getAllPublishNews(int page, int size) {
        Page<NewEntity> newEntitiesList = newsRepository.findAllByStatusTrue(PageRequest.of(page, size));

        List<NewsResponse> responseList = newEntitiesList.stream()
                .map(newEntity -> new NewsResponse(
                        newEntity.getNewId(),
                        newEntity.getNewContent(),
                        newEntity.getNewTitle(),
                        newEntity.isStatus(),
                        newEntity.getCreatedDate(),
                        newEntity.getCreatedBy()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, newEntitiesList.getPageable(), newEntitiesList.getTotalElements());
    }

    @Override
    public NewsResponse updateNews(String newsId, NewsRequest request) {
        //get room by id
        NewEntity news = newsRepository.findById(newsId).orElseThrow(
                () -> new AppException(ErrorCode.NEWS_NOT_FOUND)
        );
        news.setNewContent(request.getNewContent());
        news.setNewTitle(request.getNewTitle());
        news.setStatus(request.isStatus());
        news.setCreatedBy(request.getCreatedBy());

        newsRepository.save(news);
        return getNews(newsId);
    }

    @Override
    public NewsResponse getNews(String newsId) {
        NewEntity news = newsRepository.findById(newsId).orElseThrow(
                () -> new AppException(ErrorCode.NEWS_NOT_FOUND)
        );

        NewsResponse newsResponse = new NewsResponse();
        newsResponse.setNewId(news.getNewId());
        newsResponse.setNewContent(news.getNewContent());
        newsResponse.setNewTitle(news.getNewTitle());
        newsResponse.setStatus(news.isStatus());
        newsResponse.setCreatedBy(news.getCreatedBy());
        newsResponse.setCreateDate(news.getCreatedDate());
        return newsResponse;
    }

    @Override
        public void deleteNews(String roomId) {
        newsRepository.deleteById(roomId);
    }

    @Override
    public NewEntity getNewtById(String newsId) {
        return newsRepository.findByNewId(newsId).orElseThrow(
                () -> new AppException(ErrorCode.NEWS_NOT_FOUND)
        );
    }

}
