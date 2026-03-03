package com.miachoose.aizerocodegeneration.service.impl;

import com.miachoose.aizerocodegeneration.service.UserService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.miachoose.aizerocodegeneration.model.entity.App;
import com.miachoose.aizerocodegeneration.mapper.AppMapper;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author <a href="https://github.com/MiaChoose">MiaChoose</a>
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements UserService.AppService {

}
