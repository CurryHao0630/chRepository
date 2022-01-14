package com.markerhub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.service.BlogService;
import com.markerhub.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2021-12-30
 */
@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    //查询所有
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.succ(pageData);
    }

    //查询单个
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable("id") Long id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客不存在或已被删除");
        return Result.succ(blog);
    }

    //添加与更新
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){
        if(blog.getId() != null){ //编辑
            Long userId = blogService.getById(blog.getId()).getUserId();
            Assert.isTrue(userId.longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
        }else{ //创建
            blog.setUserId(ShiroUtil.getProfile().getId());
            blog.setCreated(LocalDateTime.now());
            blog.setStatus(0);
        }
        blogService.saveOrUpdate(blog);
        return Result.succ(null);
    }

    @RequiresAuthentication
    @GetMapping("/blog/delete")
    public Result delete(@RequestParam Integer id){
        blogService.removeById(id);
        return Result.succ(null);
    }
}
