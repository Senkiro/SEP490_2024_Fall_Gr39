package com.nsg.controller;

import com.nsg.dto.request.news.NewsRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.entity.NewEntity;
import com.nsg.service.NewsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class NewsController {
    @Autowired
    NewsService newsService;

    /**********************************
     * Manage News
     **********************************/
    @PostMapping("/create-news")
    public ApiResponse<NewsResponse> createNews(@RequestBody @Valid NewsRequest request) {
        return ApiResponse.<NewsResponse>builder()
                .result(newsService.createNews(request))
                .message("Create draft news successfully!")
                .build();
    }

    //get news
    @GetMapping("/get-all-news")
    public ApiResponse<Page<NewsResponse>> getNewsByBatch(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<NewsResponse>>builder()
                .result(newsService.getAllNews(page, size))
                .build();
    }

    //get publish news
    @GetMapping("/get-all-publish-news")
    public ApiResponse<Page<NewsResponse>> getAllPublishNews(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<NewsResponse>>builder()
                .result(newsService.getAllPublishNews(page, size))
                .build();
    }

    //update news
    @PostMapping("/update-news/{newsId}")
    public ApiResponse<NewsResponse> updateNews(@PathVariable("newsId") String newsId, @RequestBody NewsRequest request) {
        return ApiResponse.<NewsResponse>builder()
                .result(newsService.updateNews(newsId, request))
                .build();
    }

    //delete news
    @DeleteMapping("/delete-news/{newsId}")
    public ApiResponse<?> deleteNews(@PathVariable("newsId") String newsId) {
        newsService.deleteNews(newsId);
        return ApiResponse.builder()
                .message("Delete news successfully!")
                .build();
    }

    // getNewsById
    @GetMapping("/get-news")
    public ApiResponse<NewEntity> getNewsById(@RequestParam String newsId) {
        NewEntity newEntity = newsService.getNewtById(newsId);
        return ApiResponse.<NewEntity>builder()
                .result(newEntity)
                .build();
    }

}
