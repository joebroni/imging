package com.corgrimm.imgy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.core.Constants;
import com.corgrimm.imgy.ui.ImageListFragment;
import com.devspark.appmsg.AppMsg;
import com.flurry.android.FlurryAgent;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.corgrimm.imgy.core.Constants.Oauth.EXPIRED_TOKEN;
import static com.corgrimm.imgy.core.Constants.Oauth.IMGUR_CLIENT_ID;
import static com.corgrimm.imgy.core.Constants.Oauth.TOKEN_VALID;


/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 7/3/12
 * Time: 2:22 PM
 */
public class CommentDialog extends Dialog {
    Context context;
    LinearLayout shareContainer;
    TextView count;
    EditText comment;
    Button submit;
    String imageId;
    String parentId;

    public CommentDialog(Context context, String imageId, String parentId) {
        super(context, R.style.FSDialog);
        this.context = context;
        this.imageId = imageId;
        this.parentId = parentId;
        setup();
    }

    private void setup() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comment_dialog);

        setCanceledOnTouchOutside(true);

        count = (TextView) findViewById(R.id.textCount);
        comment = (EditText) findViewById(R.id.comment_text);
        submit = (Button) findViewById(R.id.submit);

        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                count.setText(Integer.toString(140 - comment.getText().length()));
            }

            public void afterTextChanged(Editable editable) {
            }
        };

        comment.addTextChangedListener(textWatcher);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComment();
            }
        });
    }

    private void submitComment() {
        int tokenStatus = ImgyApi.checkForValidAuthToken(context);
        if ( tokenStatus == TOKEN_VALID) {
            ImgyApi.postComment(context, imageId, parentId, comment.getText().toString(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    AppMsg.makeText((Activity)context, context.getString(R.string.comment_submit_success), AppMsg.STYLE_INFO).show();
                    FlurryAgent.logEvent(Constants.Flurry.NEW_COMMENT_SUBMITTED);
                    dismiss();
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText((Activity) context, context.getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }
            });
        }
        else if (tokenStatus == EXPIRED_TOKEN) {
            FlurryAgent.logEvent(Constants.Flurry.TOKEN_REFRESH_CALLED);
            ImgyApi.getAccessTokenFromRefresh(context, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText((Activity)context, context.getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    FlurryAgent.logEvent(Constants.Flurry.TOKEN_REFRESH_FAILED);
                }

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    ImgyApi.saveAuthToken(context, response);
                    submitComment();
                }
            });
        }
        else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(
                    Uri.parse(String.format("https://api.imgur.com/oauth2/authorize?client_id=%s&response_type=%s&state=%s", IMGUR_CLIENT_ID, "code", "useless")));
            context.startActivity(intent);
        }
    }
}
