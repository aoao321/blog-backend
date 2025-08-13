package com.aoao.blog.web.service.impl;

import com.aoao.blog.common.constant.RedisConstant;
import com.aoao.blog.common.domain.dos.*;
import com.aoao.blog.common.domain.mapper.*;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;
import com.aoao.blog.common.model.BasePageQuery;
import com.aoao.blog.common.model.front.vo.article.FindArticleDetailReqVO;
import com.aoao.blog.common.model.front.vo.article.FindArticleDetailRspVO;
import com.aoao.blog.common.model.front.vo.article.FindIndexArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.article.FindPreNextArticleRspVO;
import com.aoao.blog.common.model.front.vo.category.FindCategoryListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;
import com.aoao.blog.common.utils.JsonUtil;
import com.aoao.blog.web.event.ReadArticleEvent;
import com.aoao.blog.web.markdown.MarkdownHelper;
import com.aoao.blog.web.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-24-14:03
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public PageInfo<FindIndexArticlePageListRspVO> page(BasePageQuery query) {
        // 定义redisKey
        String key = RedisConstant.CACHE_ARTICLE_PAGE_KEY + query.getCurrent() + ":" + query.getSize();
        // 查询redis，如果有就返回
        String pageJSON = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(pageJSON)) {
            if ("".equals(pageJSON)) {
                // 空对象标记，避免穿透
                return new PageInfo<>(Collections.emptyList());
            }
            return JsonUtil.toBean(pageJSON, PageInfo.class);
        }
        // 开启分页
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ArticleDO> articleList = articleMapper.selectList(new QueryWrapper<ArticleDO>()
                .eq("is_publish", 1)
                .orderByDesc("weight")
                .orderByDesc("create_time"));

        if (articleList == null || articleList.isEmpty()) {
            // 写入空，设置两分钟过期时间
            stringRedisTemplate.opsForValue().set(key, null, 2, TimeUnit.MINUTES);
            return new PageInfo<>(Collections.emptyList());
        }

        List<FindIndexArticlePageListRspVO> voList = buildArticleVOListWithCategoryAndTag(articleList);

        // 构建分页信息，注意这里用 voList 构造 PageInfo
        PageInfo<FindIndexArticlePageListRspVO> pageInfo = new PageInfo<>(voList);
        // 存入redis
        stringRedisTemplate.opsForValue().set(key, JsonUtil.toJson(pageInfo));
        return pageInfo;
    }

    @Override
    public FindArticleDetailRspVO detail(FindArticleDetailReqVO findArticleDetailReqVO) {
        // 获取文章id
        Long articleId = findArticleDetailReqVO.getArticleId();
        // 查询文章表
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (articleDO == null) {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
        }
        // 查询文章内容
        ArticleContentDO articleContentDO = articleContentMapper.selectOne(new QueryWrapper<ArticleContentDO>()
                .eq("article_id", articleId));
        // DO 转 VO
        FindArticleDetailRspVO vo = FindArticleDetailRspVO.builder()
                .title(articleDO.getTitle())
                .createTime(articleDO.getCreateTime())
                .content(MarkdownHelper.convertMarkdown2Html(articleContentDO.getContent()))
                .readNum(articleDO.getReadNum())
                .build();
        // 查询文章分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);
        CategoryDO categoryDO = categoryMapper.selectById(articleCategoryRelDO.getCategoryId());
        vo.setCategoryName(categoryDO.getName());
        vo.setCategoryId(categoryDO.getId());
        // 查询标签
        List<Long> tagIds = tagMapper.selectByArticleId(articleId);
        List<TagDO> tags = tagMapper.selectList(new QueryWrapper<TagDO>().in("id", tagIds));
        List<FindTagListRspVO> tagVos = tags.stream().map(tag -> {
            FindTagListRspVO findTagListRspVO = new FindTagListRspVO();
            BeanUtils.copyProperties(tag, findTagListRspVO);
            return findTagListRspVO;
        }).collect(Collectors.toList());
        // 设置标签
        vo.setTags(tagVos);
        // 查询上下文
        // 上一篇
        ArticleDO preArticleDO = articleMapper.selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .orderByAsc(ArticleDO::getId) // 按文章 ID 升序排列
                .gt(ArticleDO::getId, articleId) // 查询比当前文章 ID 大的
                .last("limit 1"));
        if (Objects.nonNull(preArticleDO)) {
            FindPreNextArticleRspVO preArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(preArticleDO.getId())
                    .articleTitle(preArticleDO.getTitle())
                    .build();
            vo.setPreArticle(preArticleVO);
        }

        // 下一篇
        ArticleDO nextArticleDO = articleMapper.selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .orderByDesc(ArticleDO::getId) // 按文章 ID 倒序排列
                .lt(ArticleDO::getId, articleId) // 查询比当前文章 ID 小的
                .last("limit 1"));
        if (Objects.nonNull(nextArticleDO)) {
            FindPreNextArticleRspVO nextArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(nextArticleDO.getId())
                    .articleTitle(nextArticleDO.getTitle())
                    .build();
            vo.setNextArticle(nextArticleVO);
        }
        // 发布文章阅读事件
        eventPublisher.publishEvent(new ReadArticleEvent(this, articleId));

        String key = RedisConstant.CACHE_ARTICLE_VIEW_NUM_KEY;
        Object count = stringRedisTemplate.opsForHash().get(key, articleId.toString());
        long viewCount = count == null ? 0L : Long.parseLong(count.toString());
        vo.setReadNum(viewCount);
        return vo;
    }

    private List<FindIndexArticlePageListRspVO> buildArticleVOListWithCategoryAndTag(List<ArticleDO> articleList) {
        // 把 ArticleDO 转成 VO
        List<FindIndexArticlePageListRspVO> voList = articleList.stream()
                .map(article -> {
                    FindIndexArticlePageListRspVO vo = new FindIndexArticlePageListRspVO();
                    BeanUtils.copyProperties(article, vo);
                    return vo;
                }).collect(Collectors.toList());

        // 获取ids
        List<Long> articleIds = voList.stream()
                .map(vo -> vo.getId())
                .collect(Collectors.toList());

        // 根据id获取到分类
        // {name=java, articleId=3, id=2}
        List<Map<String, Object>> categoryList = categoryMapper.selectBatchByArticleIds(articleIds);
        // 将分类按 articleId 分组
        // {articleId,findCategoryListRspVO对象}
        Map<Long, FindCategoryListRspVO> articleCategoryMap = new HashMap<>();
        for (Map<String, Object> map : categoryList) {
            Long articleId = ((Number) map.get("articleId")).longValue();
            FindCategoryListRspVO findCategoryListRspVO = new FindCategoryListRspVO();
            findCategoryListRspVO.setId(((Number) map.get("id")).longValue());
            findCategoryListRspVO.setName((String) map.get("name"));
            findCategoryListRspVO.setArticlesTotal(0l);
            // 放入map中
            articleCategoryMap.put(articleId, findCategoryListRspVO);
        }
        // 从map根据articleId获取到FindCategoryListRspVO填充分类
        for (FindIndexArticlePageListRspVO vo : voList) {
            vo.setCategory(articleCategoryMap.get(vo.getId()));
        }

        // 获取标签集合
        List<Map<String, Object>> tagMapList = tagMapper.selectBatchByArticleIds(articleIds);
        // 分类
        Map<Long, List<FindTagListRspVO>> tagMap = new HashMap<>();
        for (Map<String, Object> map : tagMapList) {
            Long articleId = ((Number) map.get("articleId")).longValue();
            Long tagId = ((Number) map.get("id")).longValue();
            String name = (String) map.get("name");

            FindTagListRspVO tag = new FindTagListRspVO(tagId, name, 0l);
            tagMap.computeIfAbsent(articleId, k -> new ArrayList<>()).add(tag);
        }
        // 填充
        for (FindIndexArticlePageListRspVO vo : voList) {
            vo.setTags(tagMap.get(vo.getId()));
        }

        return voList;
    }


}
