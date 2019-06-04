package net.oscer.config.provider;

import org.brickred.socialauth.SocialAuthManager;

import java.io.Serializable;

/**
 * @author MRCHENIKE
 * @create 2019-04-28 15:20
 **/
public class AuthManager implements Serializable {

    private SocialAuthManager manager;

    public SocialAuthManager getManager() {
        return manager;
    }

    public void setManager(SocialAuthManager manager) {
        this.manager = manager;
    }
}
