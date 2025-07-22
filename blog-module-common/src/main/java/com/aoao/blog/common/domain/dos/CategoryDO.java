package com.aoao.blog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author aoao
 * @create 2025-07-21-19:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_category")
public class CategoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Boolean isDeleted;
}