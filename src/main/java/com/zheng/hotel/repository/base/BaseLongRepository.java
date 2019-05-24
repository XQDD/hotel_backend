package com.zheng.hotel.repository.base;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseLongRepository<T> extends BaseRepository<T, Long> {
}
