package com.fkisking;

import com.fkisking.sevice.BlogService;

public class BlogServiceImpl implements BlogService {
    @Override
    public Blog getById(Integer id) {
        Blog B = new Blog();
        B.setId(id);
        B.setTitle("手写rpc");
        return B;
    }
}
