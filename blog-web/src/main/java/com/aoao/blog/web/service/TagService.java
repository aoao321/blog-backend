package com.aoao.blog.web.service;

import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-25-11:17
 */
public interface TagService {
    List<FindTagListRspVO> list();

}
