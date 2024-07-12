package com.bjpowernode.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.ProdEs;
import com.bjpowernode.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * @param current
     * @param size
     * @param prodName
     * @param sort  0为综合排序（好评率倒序）， 1为销量排序（销量倒序）， 2为价格排序（价格升序）
     * @return
     */
    @Override
    public Page<ProdEs> searchProdPage(int current, int size, String prodName, int sort) {
        //关键字查询
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("prodName", prodName);
        FieldSortBuilder sortBuilder = null;
        switch (sort) {
            case 1:
                sortBuilder = SortBuilders.fieldSort("soldNum").order(SortOrder.DESC);
                break;
            case 2:
                sortBuilder = SortBuilders.fieldSort("price").order(SortOrder.ASC);
                break;
            default:
                sortBuilder = SortBuilders.fieldSort("positiveRating").order(SortOrder.DESC);
        }
        //创建本地搜索查询构造器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.withQuery(matchQueryBuilder)
                .withPageable(PageRequest.of(current, size))
                .withSort(sortBuilder)
                .build();
        SearchHits<ProdEs> search = elasticsearchRestTemplate.search(nativeSearchQuery, ProdEs.class);
        List<ProdEs> collect = search.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new Page<ProdEs>(current, size).setRecords(collect);
    }

    @Override
    public Page<ProdEs> prodListByTagId(Long tagId, String size) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        SearchHits<ProdEs> search = elasticsearchRestTemplate.search(nativeSearchQuery, ProdEs.class);
        List<ProdEs> prodEsList = search.stream().map(SearchHit::getContent).collect(Collectors.toList());
        int index = 0;
        List<ProdEs> result = new ArrayList<>();
        for (ProdEs prodEs : prodEsList) {
            if (index < Integer.parseInt(size) && prodEs.getTagList().contains(tagId)) {
                result.add(prodEs);
                index++;
            }
        }
        return new Page<ProdEs>(0, Integer.parseInt(size)).setRecords(result);
    }

    @Override
    public Page<ProdEs> pageProd(Long categoryId) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        SearchHits<ProdEs> search = elasticsearchRestTemplate.search(nativeSearchQuery, ProdEs.class);
        List<ProdEs> prodEsList = search.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new Page<ProdEs>().setRecords(
                prodEsList.stream().filter(prodEs -> categoryId.equals(prodEs.getCategoryId())).collect(Collectors.toList())
        );
    }
}
