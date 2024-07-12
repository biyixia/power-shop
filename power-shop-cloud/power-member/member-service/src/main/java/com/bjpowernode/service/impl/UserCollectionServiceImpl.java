package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.UserCollection;
import com.bjpowernode.feign.ProdFeign;
import com.bjpowernode.service.UserCollectionService;
import com.bjpowernode.mapper.UserCollectionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【user_collection】的数据库操作Service实现
 * @createDate 2024-03-17 21:12:01
 */
@Service
@RequiredArgsConstructor
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection>
        implements UserCollectionService {
    private final UserCollectionMapper userCollectionMapper;
    private final ProdFeign prodFeign;

    @Override
    public Page<Prod> getProds(Page<Prod> page) {
        List<Long> prodIdList = userCollectionMapper.selectList(
                new LambdaQueryWrapper<UserCollection>()
                        .eq(UserCollection::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
        ).stream().map(UserCollection::getProdId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(prodIdList)) {
            return new Page<Prod>();
        }
        List<Prod> prods = prodFeign.getProdByIds(prodIdList);
        return page.setRecords(prods);
    }

    @Override
    public boolean addOrCancel(Long prodId) {
        //若已经收藏则，取消收藏
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserCollection> userCollectionList = this.list(
                new LambdaQueryWrapper<UserCollection>()
                        .eq(UserCollection::getProdId, prodId)
                        .eq(UserCollection::getUserId, userId)
        );
        if (userCollectionList.size() >= 1) {
            return super.removeByIds(userCollectionList.stream().map(UserCollection::getId).collect(Collectors.toList()));
        } else {
            return
                    super.save(
                            UserCollection.builder()
                                    .prodId(prodId)
                                    .userId(SecurityContextHolder.getContext().getAuthentication().getName())
                                    .createTime(new DateTime())
                                    .build()
                    );
        }
    }
}




