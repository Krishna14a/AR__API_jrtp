package com.JRTP.service;

import com.JRTP.binding.App;
import com.JRTP.constants.AppConstants;
import com.JRTP.entities.AppEntity;
import com.JRTP.entities.UserEntity;
import com.JRTP.exception.SsaWebException;
import com.JRTP.repository.AppRepo;
import com.JRTP.repository.UserRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArServiceImpl implements ArService{
    @Autowired
    private AppRepo appRepo;

    @Autowired
    private UserRepo userRepo;

    private static final String SSA_WEB_API_URL = "https://ssa.web.app/{ssn}";
    @Override
    public String createApplication(App app) {
        try {
            WebClient webClient = WebClient.create();
            String stateName = webClient.get().uri(SSA_WEB_API_URL, app.getSsn()).retrieve()
                    .bodyToMono(String.class).block();

            if (AppConstants.RI.equals(stateName)){

                UserEntity userEntity = userRepo.findById(app.getUserId()).get();

                AppEntity appEntity = new AppEntity();
                appEntity.setUser(userEntity);

                BeanUtils.copyProperties(app, appEntity);
                appEntity= appRepo.save(appEntity);

                return "App is created with case Num :"+appEntity.getCaseNum();
            }

        }catch (Exception e){
            throw new SsaWebException(e.getMessage());
        }

        return AppConstants.INVALID_SSN;
    }

    @Override
    public List<App> fetchApps(Integer userId) {
        UserEntity userEntity = userRepo.findById(userId).get();
        Integer roleId = userEntity.getRoleId();

        List<AppEntity> appEntities = null;

        if (1 == roleId) {
            appEntities = appRepo.fetchUserApps();
        } else {
            appEntities = appRepo.fetchCwApps(userId);
        }

        List<App> apps = new ArrayList<>();

        for(AppEntity entity : appEntities) {
            App app = new App();
            BeanUtils.copyProperties(entity, apps);
            apps.add(app);
        }

        return apps;
    }

}
