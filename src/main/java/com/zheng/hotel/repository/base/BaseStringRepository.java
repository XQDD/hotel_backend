package com.zheng.hotel.repository.base;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseStringRepository<T> extends BaseRepository<T, String> {
}
