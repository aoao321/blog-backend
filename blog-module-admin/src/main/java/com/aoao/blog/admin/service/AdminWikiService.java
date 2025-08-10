package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.wiki.*;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.List;

/**
 * @author aoao
 * @create 2025-08-08-13:41
 */
public interface AdminWikiService {
    void add(AddWikiReqVO addWikiReqVO);

    void delete(DeleteWikiReqVO deleteWikiReqVO);

    PageInfo<FindWikiPageListRspVO> page(FindWikiPageListReqVO findWikiPageListReqVO);

    void top(UpdateWikiIsTopReqVO updateWikiIsTopReqVO);

    void publish(UpdateWikiIsPublishReqVO updateWikiIsPublishReqVO);

    void update(UpdateWikiReqVO updateWikiReqVO);

    List<FindWikiCatalogListRspVO> findCatalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO);

    void updateCatalog(UpdateWikiCatalogReqVO updateWikiCatalogReqVO);
}
