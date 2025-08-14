package com.aoao.blog.common.domain.dos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
* 评论表
* @TableName t_comment
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_comment")
public class CommentDO implements Serializable {

    /**
    * id
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 评论内容
    */
    private String content;

    /**
    * 头像
    */
    private String avatar;

    /**
    * 昵称
    */
    private String nickname;

    /**
    * 邮箱
    */
    private String mail;

    /**
    * 网站地址
    */
    private String website;

    /**
    * 评论所属的路由
    */
    private String routerUrl;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;

    /**
    * 最后一次更新时间
    */
    private LocalDateTime updateTime;

    /**
    * 删除标志位：0：未删除 1：已删除
    */

    private Boolean isDeleted;

    /**
    * 回复的评论 ID
    */
    private Long replyCommentId;

    /**
    * 父评论 ID
    */
    private Long parentCommentId;

    /**
    * 原因描述
    */
    private String reason;

    /**
    * 1: 待审核；2：正常；3：审核未通过;
    */
    private Integer status;


}
