package com.bjpowernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.ProdEs;

import java.util.List;

public interface SearchService {
    public Page<ProdEs> searchProdPage(int current, int size, String prodName, int sort);

    public Page<ProdEs> prodListByTagId(Long tagId, String size);

    public Page<ProdEs> pageProd(Long categoryId);
}
