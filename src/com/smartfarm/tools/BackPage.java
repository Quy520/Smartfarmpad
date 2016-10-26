package com.smartfarm.tools;

import com.smartfarm.view.AboutFragment;
import com.smartfarm.view.LightSettingFragment;
import com.smartfarm.view.LoginFragment;
import com.smartfarm.view.LoginNewFragment;
import com.smartfarm.view.LoginPhoneFragment;
import com.smartfarm.view.ModifyPasswordFragemt;
import com.smartfarm.view.ModifyPwdMsgFragment;
import com.smartfarm.view.R;
import com.smartfarm.view.RegisterFragment;
import com.smartfarm.view.SettingFragment;
import com.smartfarm.view.SettingMultiNumFragment;
import com.smartfarm.view.SettingNumFragment;
import com.smartfarm.view.SettingTestFragment;
import com.smartfarm.view.StallsFragment;
import com.smartfarm.view.TempConfigFragment;
import com.smartfarm.view.UserInfoFragment;
import com.smartfarm.view.WaterSettingFragment;


public enum BackPage {
	
	SETTING(1, R.string.setting, SettingFragment.class),
	STALLS(2, R.string.temp_info_title, StallsFragment.class),
	ILLUMINACE(3, R.string.illuminance_title, LightSettingFragment.class),
	WATER(4, R.string.water_title, WaterSettingFragment.class),
	ABOUT(5, R.string.version_info, AboutFragment.class),
	TEMP_CONFIG(6, R.string.temp_config, TempConfigFragment.class),
	LOGIN(7, R.string.user_login, LoginFragment.class),
	REGISETR(8,R.string.user_register, RegisterFragment.class),
	MODIFY(9,R.string.user_modify, ModifyPasswordFragemt.class),
	USER_INFO(10,R.string.user_info, UserInfoFragment.class),
	MSG_MODIFY(11,R.string.forget_password,ModifyPwdMsgFragment.class),
	SETTING_NUM(12,R.string.setting_num,SettingNumFragment.class),
	SETTING_NUM_MULTI(13,R.string.setting_multi_num,SettingMultiNumFragment.class),
	SETTING_TEST(14,R.string.setting_test,SettingTestFragment.class),
	LOGIN_PHONE(15,R.string.user_phone_login,LoginPhoneFragment.class),
	LOGIN_NEW(16,R.string.user_new_login,LoginNewFragment.class);
	
    private int title;
    private Class<?> clz;
    private int value;

    private BackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }
    
    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static BackPage getPageByValue(int val) {
        for (BackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }
}
