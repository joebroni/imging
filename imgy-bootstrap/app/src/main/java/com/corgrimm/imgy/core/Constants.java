

package com.corgrimm.imgy.core;

/**
 * Bootstrap constants
 */
public class Constants {

    public static class Auth {
        private Auth() {}

        /**
         * Account type id
         */
        public static final String BOOTSTRAP_ACCOUNT_TYPE = "com.corgrimm.imgy";

        /**
         * Account name
         */
        public static final String BOOTSTRAP_ACCOUNT_NAME = "imgy";

        /**
         * Provider id
         */
        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "com.corgrimm.imgy.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static class Http {
        private Http() {}



        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://api.parse.com";

        /**
         * Authentication URL
         */
        public static final String URL_AUTH = URL_BASE + "/1/login";

        /**
         * List Users URL
         */
        public static final String URL_USERS = URL_BASE + "/1/users";

        /**
         * List News URL
         */
        public static final String URL_NEWS = URL_BASE + "/1/classes/News";

        /**
         * List Checkin's URL
         */
        public static final String URL_CHECKINS = URL_BASE + "/1/classes/Locations";

        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";
        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";


    }

    public static class Vote {
        private Vote() {}

        public static final int NO_VOTE = 0;
        public static final int UPVOTE = 1;
        public static final int DOWNVOTE = 2;

        public static final String UP_VOTE_STRING = "up";
        public static final String DOWN_VOTE_STRING = "down";

    }


    public static class Extra {
        private Extra() {}

        public static final String NEWS_ITEM = "news_item";

        public static final String USER = "user";
        public static final String IMAGE = "image";
        public static final String ALBUM = "album";
        public static final String COMMENTS = "comments";
        public static final String OP = "op";
        public static final String GALLERY = "gallery";
        public static final String INDEX = "index";

    }

    public static class Prefs {
        private Prefs() {}

        public static final String PREFS_NAME = "com.corgrimm.imgy_preferences";

        public static final String VIRAL = "hot";
        public static final String USER = "user";
        public static final String SCORE = "top";

        public static final String NEWEST = "time";
        public static final String POPULAR = "viral";
        public static final String TOP = "top";

        public static final String AUTH_TOKEN = "auth_token";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String TOKEN_EXPIRE = "token_expire";

        public static final String COLLECTION = "collection";
        public static final String FILTER = "filter";
        public static final String SUBREDDIT = "subreddit";

    }

    public static class Oauth {
        private Oauth() {}

        public static final String IMGUR_CLIENT_ID = "8a24b67691c0319";
        public static final String IMGUR_CLIENT_SECRET = "3d9ee4627b1a1aac08d13b48e4f21bc693b7dced";

        public static final int TOKEN_VALID = 0;
        public static final int NO_TOKEN = 1;
        public static final int EXPIRED_TOKEN = 2;

    }

    public static class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "com.corgrimm.imgy.";
        public static final int SELECT_PHOTO = 1;

    }

    public static class Notification{
        private Notification() {}

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
    }

    public static class Flurry {

        private Flurry() {}

        public static final String FLURRY_API_KEY = "B2FMSXCPVW63R5MMPC4T";

        public static final String VIEW_GALLERY = "ViewGallery";
        public static final String VIEW_MY_IMAGES = "ViewMyImages";
        public static final String VIEW_MY_ALBUMS = "ViewMyAlbums";
        public static final String UPLOAD_IMAGE = "UploadImage";
        public static final String REFRESH_GALLERY = "RefreshGallery";
        public static final String FILTER_BUTTON_CLICKED = "FilterButtonClick";
        public static final String GALLERY_ITEM_ClICK = "GalleryItemClick";
        public static final String UP_VOTE = "Upvote";
        public static final String DOWN_VOTE = "Downvote";
        public static final String VIEW_COMMENTS = "ViewComments";
        public static final String COMMENT_EXPAND = "CommentExpand";
        public static final String COMMENT_BUTTON_CLICK = "CommentButtonClick";
        public static final String NEXT_BUTTON_CLICK = "NextButtonClick";
        public static final String PREV_BUTTON_CLICK = "PreviousButtonClick";
        public static final String IMAGE_UPLOAD_FAILURE = "ImageUploadFailed";
        public static final String LOGIN_FAILURE = "LoginFailed";
        public static final String TOKEN_REFRESH_CALLED = "TokenRefreshCalled";
        public static final String TOKEN_REFRESH_FAILED = "TokenRefreshFailed";
        public static final String VIRAL_CLICKED = "ViralClicked";
        public static final String SCORE_CLICKED = "ScoreClicked";
        public static final String USER_CLICKED = "UserSubmittedClicked";
        public static final String NEWEST_CLICKED = "NewestClicked";
        public static final String POPULAR_CLICKED = "PopularityClicked";
        public static final String SUBREDDIT = "Subreddit";
        public static final String NEW_COMMENT_SUBMITTED = "NewCommentSubmitted";
    }
}


