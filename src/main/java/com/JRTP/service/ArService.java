package com.JRTP.service;

import com.JRTP.binding.App;

import java.util.List;

public interface ArService {

    public String createApplication(App app);

    public List<App> fetchApps(Integer userId);
}
