package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.post.PostQueryRequest;
import com.rosy.main.domain.entity.Post;
import com.rosy.main.domain.vo.PostVO;

public interface IPostService extends IService<Post> {

    PostVO getPostVO(Post post);

    LambdaQueryWrapper<Post> getQueryWrapper(PostQueryRequest queryRequest);

    boolean incrementViewCount(Long postId);

    boolean incrementLikeCount(Long postId);
}
