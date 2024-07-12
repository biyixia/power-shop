package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.Category;
import com.bjpowernode.service.CategoryService;
import com.bjpowernode.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【category(产品类目)】的数据库操作Service实现
* @createDate 2024-03-17 18:51:41
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




