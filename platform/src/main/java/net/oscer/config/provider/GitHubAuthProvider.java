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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

/**
 * Implementation of GitHub provider.
 *
 * @author tsl0922@gmail.com
 */
public class GitHubAuthProvider extends AbstractProvider {

    private static final String PROFILE_URL = "https://api.github.com/user";
    private static final String EMAILS_URL = "https://api.github.com/user/emails";
    private static final Map<String, String> ENDPOINTS;
    private static final Logger LOG = LoggerFactory.getLogger(GitHubAuthProvider.class);

    private Permission scope;
    private OAuthConfig config;
    private Profile userProfile;
    private AccessGrant accessGrant;
    private OAuthStrategyBase authenticationStrategy;

    // set this to the list of extended permissions you want
    private static final String AllPerms = new String("user,public_repo,repo,gists");
    private static final String AuthenticateOnlyPerms = new String("user");

    static {
        ENDPOINTS = new HashMap<String, String>();
        ENDPOINTS.put(Constants.OAUTH_AUTHORIZATION_URL, "https://github.com/login/oauth/authorize");
        ENDPOINTS.put(Constants.OAUTH_ACCESS_TOKEN_URL, "https://github.com/login/oauth/access_token");
    }

    /**
     * Stores configuration for the provider
     *
     * @param providerConfig It contains the configuration of application like consumer key
     *                       and consumer secret
     * @throws Exception
     */
    public GitHubAuthProvider(final OAuthConfig providerConfig) throws Exception {
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
    public Profile verifyResponse(final Map<String, String> requestParams) throws Exception {
        return doVerifyResponse(requestParams);
    }

    private Profile doVerifyResponse(final Map<String, String> requestParams) throws Exception {
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
        LOG.warn("WARNING: Not implemented for GitHub");
        throw new SocialAuthException("Update Status is not implemented for GitHub");
    }

    @Override
    public Response uploadImage(String message, String fileName, InputStream inputStream) throws Exception {
        LOG.warn("WARNING: Not implemented for GitHub");
        throw new SocialAuthException("Upload Image is not implemented for GitHub");
    }

    /**
     * Logout
     */
    @Override
    public void logout() {
        accessGrant = null;
        authenticationStrategy.logout();
    }

    private Profile getProfile() throws Exception {
        Profile p = new Profile();
        Response serviceResponse;
        try {
            serviceResponse = authenticationStrategy.executeFeed(PROFILE_URL);
        } catch (Exception e) {
            throw new SocialAuthException("Failed to retrieve the user profile from  " + PROFILE_URL, e);
        }

        String result;
        try {
            result = serviceResponse.getResponseBodyAsString(Constants.ENCODING);
            LOG.debug("User Profile :" + result);
        } catch (Exception e) {
            throw new SocialAuthException("Failed to read response from  " + PROFILE_URL, e);
        }
        try {
            JSONObject resp = new JSONObject(result);
            if (resp.has("id") && resp.get("id")!=null) {
                p.setValidatedId(resp.get("id").toString());
            }
            if (resp.has("login") && resp.get("login")!=null) {
                p.setDisplayName(resp.get("login").toString());
            }
            if (resp.has("name") && resp.get("name")!=null) {
                p.setFullName(resp.get("name").toString());
            }
            if (resp.has("location") && resp.get("location")!=null) {
                p.setLocation(resp.get("location").toString());
            }
            if (resp.has("gender") && resp.get("gender")!=null) {
                p.setGender(resp.get("gender").toString());
            }
            if (resp.has("avatar_url") && resp.get("avatar_url")!=null) {
                p.setProfileImageURL(resp.get("avatar_url").toString());
            }
            if (resp.has("email") && resp.get("email")!=null) {
                p.setEmail(resp.get("email").toString());
            }
            //如果用户不设置公开email,上面是拿不到email的，还得继续请求获取email
            if (p.getEmail() == null || p.getEmail().isEmpty() || p.getEmail().equals("null")) {
                p.setEmail(getEmail());
            }
            serviceResponse.close();
            p.setProviderId(getProviderId());
            userProfile = p;
            return p;
        } catch (Exception e) {
            throw new SocialAuthException("Failed to parse the user profile json : " + result, e);
        }
    }

    /**
     * Retrieve the first valid email
     *
     * @return
     * @throws Exception
     */
    private String getEmail() throws Exception {
        Response serviceResponse;
        try {
            serviceResponse = authenticationStrategy.executeFeed(EMAILS_URL);
        } catch (Exception e) {
            throw new SocialAuthException("Failed to retrieve the user emails from  " + EMAILS_URL, e);
        }

        String result;
        String email = "";
        try {
            result = serviceResponse.getResponseBodyAsString(Constants.ENCODING);
            LOG.debug("User Emails :" + result);
        } catch (Exception e) {
            throw new SocialAuthException("Failed to read response from  " + EMAILS_URL, e);
        }
        try {
            JSONArray array = new JSONArray(result);
            int length = array.length();

            for (int i = 0; i < length; i++) {
                email = (String) array.getJSONObject(i).get("email");
                if (!email.isEmpty()) {
                    break;
                }
            }
            serviceResponse.close();
        } catch (Exception e) {
            throw new SocialAuthException("Failed to parse the user emails json : " + result, e);
        }

        return email;
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
                        final Map<String, String> headerParams, final String body) throws Exception {
        LOG.debug("Calling URL : " + url);
        Response serviceResponse;
        try {
            serviceResponse = authenticationStrategy.executeFeed(url, methodType, params, headerParams, body);
        } catch (Exception e) {
            throw new SocialAuthException("Error while making request to URL : " + url, e);
        }
        if (serviceResponse.getStatus() != 200) {
            LOG.debug("Return status for URL " + url + " is " + serviceResponse.getStatus());
            throw new SocialAuthException("Error while making request to URL :" + url + "Status : " + serviceResponse.getStatus());
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
