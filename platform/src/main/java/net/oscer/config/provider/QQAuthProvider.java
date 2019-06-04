package net.oscer.config.provider;

import org.brickred.socialauth.AbstractProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.exception.SocialAuthException;
import org.brickred.socialauth.oauthstrategy.OAuth2;
import org.brickred.socialauth.oauthstrategy.OAuthStrategyBase;
import org.brickred.socialauth.util.AccessGrant;
import org.brickred.socialauth.util.Constants;
import org.brickred.socialauth.util.OAuthConfig;
import org.brickred.socialauth.util.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

/**
 * Implementation of QQ provider.
 *
 * @author tsl0922@gmail.com
 */
public class QQAuthProvider extends AbstractProvider {

    private static final String PROFILE_URL = "https://graph.qq.com/user/get_user_info";
    private static final String OPENID_URL = "https://graph.qq.com/oauth2.0/me";
    private static final Map<String, String> ENDPOINTS;
    private static final Logger LOG = LoggerFactory.getLogger(QQAuthProvider.class);

    private String openid;
    private Permission scope;
    private OAuthConfig config;
    private Profile userProfile;
    private AccessGrant accessGrant;
    private OAuthStrategyBase authenticationStrategy;

    // set this to the list of extended permissions you want
    private static final String AllPerms = new String(
            "user,public_repo,repo,gists,get_info,add_t");
    private static final String AuthenticateOnlyPerms = new String("user");

    static {
        ENDPOINTS = new HashMap<String, String>();
        ENDPOINTS.put(Constants.OAUTH_AUTHORIZATION_URL,
                "https://graph.qq.com/oauth2.0/authorize");
        ENDPOINTS.put(Constants.OAUTH_ACCESS_TOKEN_URL,
                "https://graph.qq.com/oauth2.0/token");
    }

    /**
     * Stores configuration for the provider
     *
     * @param providerConfig It contains the configuration of application like consumer key
     *                       and consumer secret
     * @throws Exception
     */
    public QQAuthProvider(final OAuthConfig providerConfig) throws Exception {
        config = providerConfig;
        if (config.getCustomPermissions() != null) {
            this.scope = Permission.CUSTOM;
        }
        authenticationStrategy = new OAuth2(config, ENDPOINTS);
        authenticationStrategy.setPermission(scope);
        authenticationStrategy.setScope(getScope());
    }

    /**
     * Stores access grant for the provider
     *
     * @param accessGrant It contains the access token and other information
     */
    @Override
    public void setAccessGrant(final AccessGrant accessGrant) {
        this.accessGrant = accessGrant;
        scope = accessGrant.getPermission();
        authenticationStrategy.setAccessGrant(accessGrant);
    }

    /**
     * This is the most important action. It redirects the browser to an
     * appropriate URL which will be used for authentication with the provider
     * that has been set using setId()
     *
     * @throws Exception
     */

    @Override
    public String getLoginRedirectURL(final String successUrl) throws Exception {
        return authenticationStrategy.getLoginRedirectURL(successUrl);
    }

    /**
     * Verifies the user when the external provider redirects back to our
     * application.
     *
     * @param requestParams request parameters, received from the provider
     * @return Profile object containing the profile information
     * @throws Exception
     */
    @Override
    public Profile verifyResponse(final Map<String, String> requestParams)
            throws Exception {
        return doVerifyResponse(requestParams);
    }

    private Profile doVerifyResponse(final Map<String, String> requestParams)
            throws Exception {
        LOG.info("Retrieving Access Token in verify response function");

        accessGrant = authenticationStrategy.verifyResponse(requestParams);

        if (accessGrant != null) {
            LOG.debug("Obtaining user profile");
            return getProfile();
        } else {
            throw new SocialAuthException("Unable to get Access token");
        }
    }

    /**
     * Gets the list of contacts of the user and their email.
     *
     * @return List of profile objects representing Contacts. Only name and
     * email will be available
     * @throws Exception
     */

    @Override
    public List<Contact> getContactList() throws Exception {
        return null;
    }


    /**
     * Updates the status on the chosen provider if available. This may not be
     * implemented for all providers.
     *
     * @param msg Message to be shown as user's status
     * @throws Exception
     */
    @Override
    public Response updateStatus(final String msg) throws Exception {
        LOG.warn("WARNING: Not implemented for QQ");
        throw new SocialAuthException("Update Status is not implemented for QQ");
    }

    @Override
    public Response uploadImage(String message, String fileName, InputStream inputStream) throws Exception {
        LOG.warn("WARNING: Not implemented for QQ");
        throw new SocialAuthException("Upload Image is not implemented for QQ");
    }

    /**
     * Logout
     */
    @Override
    public void logout() {
        accessGrant = null;
        authenticationStrategy.logout();
    }


    private synchronized Profile getProfile() throws Exception {
        Profile p = new Profile();
        Response serviceResponse;
        // 先获取openid
        try {
            serviceResponse = authenticationStrategy.executeFeed(OPENID_URL
                    + "?format=json");
        } catch (Exception e) {
            throw new SocialAuthException(
                    "Failed to retrieve the user profile from  " + PROFILE_URL,
                    e);
        }
        String openid_result;
        try {
            openid_result = serviceResponse
                    .getResponseBodyAsString(Constants.ENCODING);
            LOG.debug("User OPENID :" + openid_result);
        } catch (Exception e) {
            throw new SocialAuthException("Failed to read response from  "
                    + OPENID_URL, e);
        }
        JSONObject openid_resp = new JSONObject(openid_result.replace(
                "callback( ", "").replace(");", ""));
        if (openid_resp.has("openid")) {
            this.openid = openid_resp.getString("openid");
        } else {
            throw new SocialAuthException("Failed to parse the openid json : " + openid_result);
        }

        // 再获取个人信息
        try {
            serviceResponse = authenticationStrategy.executeFeed(PROFILE_URL
                    + "?openid=" + openid + "&oauth_consumer_key="
                    + config.get_consumerKey());
        } catch (Exception e) {
            throw new SocialAuthException(
                    "Failed to retrieve the user profile from  " + PROFILE_URL,
                    e);
        }

        String result;
        try {
            result = serviceResponse
                    .getResponseBodyAsString(Constants.ENCODING);
            LOG.debug("User Profile :" + result);
        } catch (Exception e) {
            throw new SocialAuthException("Failed to read response from  "
                    + PROFILE_URL, e);
        }
        try {
            //api http://wiki.open.qq.com/wiki/mobile/get_simple_userinfo
            JSONObject resp = new JSONObject(result);

            //获取省份与城市
            StringBuffer location = new StringBuffer("");
            location.append(resp.getString("province")).append("_").append(resp.getString("city"));
            p.setLocation(location.toString());

            p.setFirstName(resp.getString("nickname"));
            if ("男".equals(resp.getString("gender"))) {
                p.setGender("Male");

            } else if ("女".equals(resp.getString("gender"))) {
                p.setGender("Female");
            }
            p.setValidatedId(openid);
            p.setEmail(openid);
            p.setProfileImageURL(resp.getString("figureurl_2"));

            serviceResponse.close();
            p.setProviderId(getProviderId());
            userProfile = p;
            return p;
        } catch (Exception e) {
            throw new SocialAuthException(
                    "Failed to parse the user profile json : " + result, e);
        }
    }

    /**
     * @param p Permission object which can be Permission.AUHTHENTICATE_ONLY,
     *          Permission.ALL, Permission.DEFAULT
     */
    @Override
    public void setPermission(final Permission p) {
        this.scope = p;
    }

    /**
     * Makes HTTP request to a given URL.
     *
     * @param url          URL to make HTTP request.
     * @param methodType   Method type can be GET, POST or PUT
     * @param params       Any additional parameters whose signature need to compute.
     *                     Only used in case of "POST" and "PUT" method type.
     * @param headerParams Any additional parameters need to pass as Header Parameters
     * @param body         Request Body
     * @return Response object
     * @throws Exception
     */
    @Override
    public Response api(final String url, final String methodType,
                        final Map<String, String> params,
                        final Map<String, String> headerParams, final String body)
            throws Exception {
        LOG.debug("Calling URL : " + url);
        Response serviceResponse;
        try {
            serviceResponse = authenticationStrategy.executeFeed(url,
                    methodType, params, headerParams, body);
        } catch (Exception e) {
            throw new SocialAuthException(
                    "Error while making request to URL : " + url, e);
        }
        if (serviceResponse.getStatus() != 200) {
            LOG.debug("Return status for URL " + url + " is "
                    + serviceResponse.getStatus());
            throw new SocialAuthException("Error while making request to URL :"
                    + url + "Status : " + serviceResponse.getStatus());
        }
        return serviceResponse;
    }

    /**
     * Retrieves the user profile.
     *
     * @return Profile object containing the profile information.
     */
    @Override
    public Profile getUserProfile() throws Exception {
        if (userProfile == null && accessGrant != null) {
            getProfile();
        }
        return userProfile;
    }

    @Override
    public AccessGrant getAccessGrant() {
        return accessGrant;
    }

    @Override
    public String getProviderId() {
        return config.getId();
    }

    private String getScope() {
        String scopeStr = null;
        if (Permission.AUTHENTICATE_ONLY.equals(scope)) {
            scopeStr = AuthenticateOnlyPerms;
        } else if (Permission.CUSTOM.equals(scope)) {
            scopeStr = config.getCustomPermissions();
        } else {
            scopeStr = AllPerms;
        }
        return scopeStr;
    }

    @Override
    protected List<String> getPluginsList() {
        List<String> list = new ArrayList<String>();
        if (config.getRegisteredPlugins() != null
                && config.getRegisteredPlugins().length > 0) {
            list.addAll(Arrays.asList(config.getRegisteredPlugins()));
        }
        return list;
    }

    @Override
    protected OAuthStrategyBase getOauthStrategy() {
        return authenticationStrategy;
    }
}
